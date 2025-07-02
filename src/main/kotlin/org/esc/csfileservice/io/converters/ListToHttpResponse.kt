package org.esc.csfileservice.io.converters

import org.esc.csfileservice.io.BasicSuccessfulResponse

fun <T> List<T>.toHttpResponse(): BasicSuccessfulResponse<List<T>> {
    return BasicSuccessfulResponse(this)
}