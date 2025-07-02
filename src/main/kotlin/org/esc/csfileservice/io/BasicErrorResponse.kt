package org.esc.csfileservice.io

data class BasicErrorResponse(override val status: Int, override val message: String) :
    AbstractResponse<String>(status, message)
