package org.careerseekers.csfileservice.entities

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime
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
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
)