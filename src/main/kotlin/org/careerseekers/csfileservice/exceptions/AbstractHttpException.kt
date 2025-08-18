package org.careerseekers.csfileservice.exceptions

abstract class AbstractHttpException(val status: Int, override val message: String?) : RuntimeException(message)