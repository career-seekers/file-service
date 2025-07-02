package org.esc.csfileservice.io

import org.springframework.http.HttpStatus

data class BasicSuccessfulResponse<T>(
    override val message: T
) : AbstractResponse<T>(HttpStatus.OK.value(), message)