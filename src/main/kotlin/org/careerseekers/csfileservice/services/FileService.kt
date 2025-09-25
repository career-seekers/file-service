package org.careerseekers.csfileservice.services

import org.careerseekers.csfileservice.dto.CloudFileSavingDto
import org.careerseekers.csfileservice.entities.FilesStorage
import org.careerseekers.csfileservice.enums.FileTypes
import org.careerseekers.csfileservice.enums.KafkaFileTypes
import org.careerseekers.csfileservice.exceptions.NotFoundException
import org.careerseekers.csfileservice.io.BasicSuccessfulResponse
import org.careerseekers.csfileservice.io.converters.toHttpResponse
import org.careerseekers.csfileservice.repositories.FilesStorageRepository
import org.careerseekers.csfileservice.services.kafka.producers.CloudFileSavingKafkaProducer
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.PathResource
import org.springframework.core.io.Resource
import org.springframework.http.codec.multipart.FilePart
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.util.StringUtils
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.UUID

@Service
class FileService(
    @param:Value("\${storage.location}") val rootLocation: String,
    private val filesStorageRepository: FilesStorageRepository,
    private val cloudFileSavingKafkaProducer: CloudFileSavingKafkaProducer,
) {
    private val rootPath: Path = Paths.get(rootLocation).toAbsolutePath().normalize()

    init {
        try {
            Files.createDirectories(rootPath)
        } catch (ex: IOException) {
            throw RuntimeException("Не удалось создать директорию для хранения файлов: $rootLocation", ex)
        }
    }

    fun getAllFiles(): Flux<FilesStorage> {
        return filesStorageRepository.findAll()
    }

    fun getFileById(id: Long): Mono<FilesStorage> {
        return filesStorageRepository.findById(id)
            .switchIfEmpty(Mono.error(NotFoundException("Файл с ID $id не найден.")))
    }

    fun getFileContentById(id: Long): Mono<Resource> {
        return filesStorageRepository.findById(id)
            .switchIfEmpty(Mono.error(NotFoundException("Файл с ID $id не найден.")))
            .flatMap { fileStorage ->
                val path = Paths.get(fileStorage.filePath)
                if (!Files.exists(path)) {
                    Mono.error(NotFoundException("Файл по пути $path не найден"))
                } else {
                    Mono.just(PathResource(path))
                }
            }
    }

    @Transactional
    fun saveFile(file: FilePart, fileType: FileTypes): Mono<FilesStorage> {
        val originalFilename = StringUtils.cleanPath(file.filename())
        val extension = originalFilename.substringAfterLast('.', "")
        val storedFilename = UUID.randomUUID()

        return store(file, storedFilename, extension)
            .flatMap { filePath ->
                val entity = FilesStorage(
                    originalFilename = originalFilename,
                    storedFilename = storedFilename,
                    contentType = file.headers().contentType?.toString() ?: "application/octet-stream",
                    fileType = fileType.name,
                    filePath = filePath,
                )
                filesStorageRepository.save(entity)
            }
            .flatMap { savedEntity ->
                Mono.fromRunnable<CloudFileSavingDto> {
                    cloudFileSavingKafkaProducer.sendMessage(
                        CloudFileSavingDto(
                            filePath = savedEntity.filePath,
                            filename = "${storedFilename}.${getFileExtension(savedEntity.originalFilename)}",
                            fileType = KafkaFileTypes.DOCUMENT
                        )
                    )
                }.subscribeOn(Schedulers.boundedElastic())
                    .thenReturn(savedEntity)
            }
    }

    private fun getFileExtension(filename: String): String? {
        val index = filename.lastIndexOf('.')
        return if (index != -1 && index != filename.length - 1) {
            filename.substring(index + 1)
        } else {
            null
        }
    }

    @Transactional
    fun verifyFile(id: Long): Mono<String> {
        return getFileById(id)
            .flatMap { fileStorageItem ->
                fileStorageItem.verified = true
                filesStorageRepository.save(fileStorageItem)
                    .thenReturn("File verified successfully.")
            }
    }

    @Transactional
    fun deleteById(id: Long): Mono<BasicSuccessfulResponse<String>> {
        return getFileById(id)
            .flatMap { fileStorage ->
                val path = Paths.get(fileStorage.filePath)

                Mono.fromCallable { Files.deleteIfExists(path) }
                    .subscribeOn(Schedulers.boundedElastic())
                    .then(filesStorageRepository.deleteById(id))
                    .thenReturn("Файл с ID $id удалён успешно.".toHttpResponse())
            }
    }

    private fun store(file: FilePart, storedFilename: UUID, extension: String): Mono<String> {
        val uniqueName = storedFilename.toString() + if (extension.isNotEmpty()) ".$extension" else ""
        val destinationFile = rootPath.resolve(uniqueName).normalize()

        if (!destinationFile.startsWith(rootPath)) {
            return Mono.error(RuntimeException("Неверный путь для сохранения файла: $uniqueName"))
        }

        return file.transferTo(destinationFile)
            .thenReturn(destinationFile.toString())
    }
}