package org.esc.csfileservice.entities

import org.esc.csfileservice.enums.FileTypes
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.util.UUID

@Table("files_storage")
data class FilesStorage(
    @Id
    val id: Long? = null,
    val originalFilename: String,
    val storedFilename: UUID,
    val contentType: String,
    val size: Long,
    val fileType: FileTypes,
    val filePath: String,
)