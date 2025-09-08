package org.careerseekers.csfileservice.entities

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.util.Date
import java.util.UUID

@Table("files_storage")
data class FilesStorage(
    @Id
    val id: Long? = null,
    val originalFilename: String,
    val storedFilename: UUID,
    val contentType: String,
    val fileType: String,
    val filePath: String,
    val createdAt: Date,
    val updatedAt: Date,
)