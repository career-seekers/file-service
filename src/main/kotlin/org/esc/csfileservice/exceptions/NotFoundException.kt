package org.esc.csfileservice.exceptions

import org.springframework.http.HttpStatus

class NotFoundException(override val message: String) : AbstractHttpException(HttpStatus.NOT_FOUND.value(), message)