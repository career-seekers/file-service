package org.esc.csfileservice.dto.files

import org.esc.csfileservice.dto.DtoClass
import org.esc.csfileservice.enums.FileTypes
import org.springframework.web.multipart.MultipartFile

data class SaveFileDto(
    val file: MultipartFile,
    val fileType: FileTypes,
) : DtoClass
