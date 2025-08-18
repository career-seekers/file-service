@file:Suppress("UNCHECKED_CAST")

package org.careerseekers.csfileservice.io.converters

import org.careerseekers.csfileservice.io.BasicSuccessfulResponse

interface ConvertableToHttpResponse<T : ConvertableToHttpResponse<T>> {
    fun toHttpResponse(): BasicSuccessfulResponse<T> = BasicSuccessfulResponse(this as T)
}