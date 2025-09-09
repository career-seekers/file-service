package org.careerseekers.csfileservice.controllers

import org.careerseekers.csfileservice.entities.FilesStorage
import org.careerseekers.csfileservice.enums.FileTypes
import org.careerseekers.csfileservice.services.FileService
import org.springframework.http.MediaType
import org.springframework.http.codec.multipart.FilePart
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/file-service/v1/files")
class DirectionsDocumentsController(private val fileService: FileService) {

    @PostMapping("/uploadDirectionsDocument", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun loadDocument(
        @RequestPart("file") file: FilePart,
        @RequestPart("type") type: String,
    ): Mono<FilesStorage> {
        return fileService.saveFile(file, FileTypes.valueOf(type.uppercase().trim().trim('"')))
    }
}