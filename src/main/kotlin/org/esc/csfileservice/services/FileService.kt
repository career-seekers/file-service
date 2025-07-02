package org.esc.csfileservice.services

import org.esc.csfileservice.dto.files.SaveFileDto
import org.esc.csfileservice.entities.FilesStorage
import org.esc.csfileservice.repositories.FilesStorageRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.util.StringUtils
import org.springframework.web.multipart.MultipartFile
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.util.UUID

@Service
class FileService(
    @Value("\${storage.location}") private val rootLocation: String,
    private val filesStorageRepository: FilesStorageRepository
) {
    private val rootPath: Path = Paths.get(rootLocation).toAbsolutePath().normalize()

    init {
        try {
            Files.createDirectories(rootPath)
        } catch (ex: IOException) {
            throw RuntimeException("Не удалось создать директорию для хранения файлов: $rootLocation", ex)
        }
    }

    @Transactional
    fun saveFile(item: SaveFileDto): Mono<FilesStorage> {
        val originalFilename = StringUtils.cleanPath(item.file.originalFilename ?: "file")
        val extension = originalFilename.substringAfterLast('.', "")
        val storedFilename = UUID.randomUUID()

        return store(item.file, storedFilename, extension)
            .flatMap { filePath ->
                val entity = FilesStorage(
                    id = null,
                    originalFilename = originalFilename,
                    storedFilename = storedFilename,
                    contentType = item.file.contentType ?: "application/octet-stream",
                    size = item.file.size,
                    fileType = item.fileType,
                    filePath = filePath
                )
                filesStorageRepository.save(entity)
            }
    }

    private fun store(file: MultipartFile, storedFilename: UUID, extension: String): Mono<String> {
        val uniqueName = storedFilename.toString() + if (extension.isNotEmpty()) ".$extension" else ""
        val destinationFile = rootPath.resolve(uniqueName).normalize()

        if (!destinationFile.startsWith(rootPath)) {
            return Mono.error(RuntimeException("Неверный путь для сохранения файла: $uniqueName"))
        }

        return Mono.fromCallable {
            file.inputStream.use { input ->
                Files.copy(input, destinationFile, StandardCopyOption.REPLACE_EXISTING)
            }
            destinationFile.toString()
        }
            .subscribeOn(Schedulers.boundedElastic())
    }
}