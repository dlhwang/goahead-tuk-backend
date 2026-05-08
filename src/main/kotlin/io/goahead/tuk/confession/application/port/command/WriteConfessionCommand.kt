package io.goahead.tuk.confession.application.port.command

data class WriteConfessionCommand(
    val authorId: String,
    val content: String,
)
