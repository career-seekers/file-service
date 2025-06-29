package org.esc.csfileservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class CsFileServiceApplication

fun main(args: Array<String>) {
    runApplication<CsFileServiceApplication>(*args)
}
