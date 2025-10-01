package org.careerseekers.csfileservice.controllers

import org.careerseekers.csfileservice.entities.FilesStorage
import org.careerseekers.csfileservice.enums.FileTypes
import org.careerseekers.csfileservice.io.BasicSuccessfulResponse
import org.careerseekers.csfileservice.services.FileService
import org.springframework.core.io.Resource
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.http.codec.multipart.FilePart
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/file-service/v1/files")
class FilesController(private val fileService: FileService) {

    @GetMapping("/")
    fun getAll(): Flux<FilesStorage> {
        return fileService.getAllFiles()
    }

    @GetMapping("/{id}")
    fun getById(@PathVariable("id") id: Long): Mono<FilesStorage> {
        return fileService.getFileById(id)
    }

    @GetMapping("/view/{id}")
    fun viewFile(@PathVariable id: Long): Mono<ResponseEntity<Resource>> {
        return fileService.getFileById(id)
            .flatMap { fileStorage ->
                fileService.getFileContentById(id)
                    .map { resource ->
                        val contentType = try {
                            MediaType.parseMediaType(fileStorage.contentType)
                        } catch (_: Exception) {
                            MediaType.APPLICATION_OCTET_STREAM
                        }

                        ResponseEntity.ok()
                            .contentType(contentType)
                            .body(resource)
                    }
            }
            .defaultIfEmpty(ResponseEntity.notFound().build())
            .onErrorResume { ex ->
                Mono.error(ex)
            }
    }


    @GetMapping("/download/{id}")
    fun downloadFile(@PathVariable id: Long): Mono<ResponseEntity<Resource>> {
        return fileService.getFileById(id)
            .flatMap { fileStorage ->
                fileService.getFileContentById(id)
                    .map { resource ->
                        val contentType = MediaType.parseMediaType(fileStorage.contentType)

                        ResponseEntity.ok()
                            .contentType(contentType)
                            .header(
                                HttpHeaders.CONTENT_DISPOSITION,
                                "attachment; filename=\"${fileStorage.originalFilename}\""
                            )
                            .body(resource)
                    }
            }
            .defaultIfEmpty(ResponseEntity.notFound().build())
            .onErrorResume { ex ->
                Mono.error(ex)
            }
    }

    @PostMapping(
        "uploadAvatar",
        consumes = [MediaType.MULTIPART_FORM_DATA_VALUE]
    )
    fun uploadAvatar(@RequestPart("file") file: FilePart): Mono<FilesStorage> {
        return fileService.saveFile(file, FileTypes.AVATAR)
    }

    @PostMapping(
        "uploadSnils",
        consumes = [MediaType.MULTIPART_FORM_DATA_VALUE]
    )
    fun uploadSnils(@RequestPart("file") file: FilePart): Mono<FilesStorage> {
        return fileService.saveFile(file, FileTypes.SNILS)
    }

    @PostMapping(
        "uploadStudyingCertificate",
        consumes = [MediaType.MULTIPART_FORM_DATA_VALUE]
    )
    fun uploadStudyingCertificate(@RequestPart("file") file: FilePart): Mono<FilesStorage> {
        return fileService.saveFile(file, FileTypes.STUDYING_CERTIFICATE)
    }

    @PostMapping(
        "uploadAdditionalStudyingCertificate",
        consumes = [MediaType.MULTIPART_FORM_DATA_VALUE]
    )
    fun uploadAdditionalStudyingCertificate(@RequestPart("file") file: FilePart): Mono<FilesStorage> {
        return fileService.saveFile(file, FileTypes.ADDITIONAL_STUDYING_CERTIFICATE)
    }

    @PostMapping(
        "uploadConsentToChildPDP",
        consumes = [MediaType.MULTIPART_FORM_DATA_VALUE]
    )
    fun uploadConsentToChildPDP(@RequestPart("file") file: FilePart): Mono<FilesStorage> {
        return fileService.saveFile(file, FileTypes.CONSENT_TO_CHILD_PDP)
    }

    @PostMapping(
        "uploadConsentToMentorPDP",
        consumes = [MediaType.MULTIPART_FORM_DATA_VALUE]
    )
    fun uploadConsentToMentorPDP(@RequestPart("file") file: FilePart): Mono<FilesStorage> {
        return fileService.saveFile(file, FileTypes.CONSENT_TO_MENTOR_PDP)
    }

    @PostMapping(
        "uploadConsentToTutorPDP",
        consumes = [MediaType.MULTIPART_FORM_DATA_VALUE]
    )
    fun uploadConsentToTutorPDP(@RequestPart("file") file: FilePart): Mono<FilesStorage> {
        return fileService.saveFile(file, FileTypes.CONSENT_TO_TUTOR_PDP)
    }

    @PostMapping(
        "uploadConsentToExpertPDP",
        consumes = [MediaType.MULTIPART_FORM_DATA_VALUE]
    )
    fun uploadConsentToExpertPDP(@RequestPart("file") file: FilePart): Mono<FilesStorage> {
        return fileService.saveFile(file, FileTypes.CONSENT_TO_EXPERT_PDP)
    }

    @PostMapping(
        "uploadBirthCertificate",
        consumes = [MediaType.MULTIPART_FORM_DATA_VALUE]
    )
    fun uploadBirthCertificate(@RequestPart("file") file: FilePart): Mono<FilesStorage> {
        return fileService.saveFile(file, FileTypes.BIRTH_CERTIFICATE)
    }

    @PostMapping(
        "uploadDirectionIcon",
        consumes = [MediaType.MULTIPART_FORM_DATA_VALUE]
    )
    fun uploadDirectionIcon(@RequestPart("file") file: FilePart): Mono<FilesStorage> {
        return fileService.saveFile(file, FileTypes.DIRECTION_ICON)
    }

    @PatchMapping("/verify/{id}/{verification}")
    fun verify(@PathVariable id: Long, @PathVariable verification: Boolean): Mono<BasicSuccessfulResponse<String>> =
        fileService.verifyFile(id, verification)

    @DeleteMapping("/{id}")
    fun deleteById(@PathVariable id: Long): Mono<BasicSuccessfulResponse<String>> = fileService.deleteById(id)

}