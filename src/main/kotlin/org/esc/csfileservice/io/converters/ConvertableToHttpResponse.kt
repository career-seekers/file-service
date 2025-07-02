@file:Suppress("UNCHECKED_CAST")

package org.esc.csfileservice.io.converters

import org.esc.csfileservice.io.BasicSuccessfulResponse

interface ConvertableToHttpResponse<T : ConvertableToHttpResponse<T>> {
    fun toHttpResponse(): BasicSuccessfulResponse<T> = BasicSuccessfulResponse(this as T)
}