package org.esc.csfileservice.services

import org.esc.csfileservice.entities.FilesStorage
import org.esc.csfileservice.enums.FileTypes
import org.esc.csfileservice.repositories.FilesStorageRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.codec.multipart.FilePart
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.util.StringUtils
import reactor.core.publisher.Mono
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
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