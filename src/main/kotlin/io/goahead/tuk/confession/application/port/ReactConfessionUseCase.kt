package io.goahead.tuk.confession.application.port

import io.goahead.tuk.confession.application.port.command.ReactConfessionCommand

interface ReactConfessionUseCase {
    fun execute(command: ReactConfessionCommand);
}