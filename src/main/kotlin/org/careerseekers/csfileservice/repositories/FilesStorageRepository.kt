package org.careerseekers.csfileservice.repositories

import org.careerseekers.csfileservice.entities.FilesStorage
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface FilesStorageRepository : ReactiveCrudRepository<FilesStorage, Long>