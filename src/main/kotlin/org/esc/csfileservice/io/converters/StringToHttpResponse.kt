package org.esc.csfileservice.io.converters

import org.esc.csfileservice.io.BasicSuccessfulResponse

fun String.toHttpResponse(): BasicSuccessfulResponse<String> {
    return BasicSuccessfulResponse(this)
}