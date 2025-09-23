package org.careerseekers.csfileservice.enums

enum class KafkaFileTypes(private val alias: String) {
    DB_DUMP("db_dump"),
    DOCUMENT("docs");

    fun KafkaFileTypes.getAlias() = this.alias
}