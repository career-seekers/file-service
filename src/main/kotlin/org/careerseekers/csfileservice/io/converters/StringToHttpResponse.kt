package org.careerseekers.csfileservice.io.converters

import org.careerseekers.csfileservice.io.BasicSuccessfulResponse

fun String.toHttpResponse(): BasicSuccessfulResponse<String> {
    return BasicSuccessfulResponse(this)
}