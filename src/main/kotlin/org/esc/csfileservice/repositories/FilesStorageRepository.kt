package org.esc.csfileservice.repositories

import org.esc.csfileservice.entities.FilesStorage
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface FilesStorageRepository : ReactiveCrudRepository<FilesStorage, Long>