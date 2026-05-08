package io.goahead.tuk.confession.application.port.command

data class ReactConfessionCommand(
    val confessionId: String,
    val type: String,
)
