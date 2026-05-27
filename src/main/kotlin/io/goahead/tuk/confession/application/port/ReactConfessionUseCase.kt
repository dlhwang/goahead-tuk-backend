package io.goahead.tuk.confession.application.port

import io.goahead.tuk.confession.application.port.command.ReactConfessionCommand

interface ReactConfessionUseCase {
    fun select(command: ReactConfessionCommand)
    fun clear(command: ReactConfessionCommand)
}
