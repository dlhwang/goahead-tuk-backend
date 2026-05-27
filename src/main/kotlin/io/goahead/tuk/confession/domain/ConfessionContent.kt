package io.goahead.tuk.confession.domain

data class ConfessionContent(
    val value: String
) {
    init {
        require(value.isNotBlank() && value.length <= 1000) { "Invalid confession content" }
    }
}
