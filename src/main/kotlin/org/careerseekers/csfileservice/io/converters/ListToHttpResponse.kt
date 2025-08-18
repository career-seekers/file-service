package org.careerseekers.csfileservice.io.converters

import org.careerseekers.csfileservice.io.BasicSuccessfulResponse

fun <T> List<T>.toHttpResponse(): BasicSuccessfulResponse<List<T>> {
    return BasicSuccessfulResponse(this)
}