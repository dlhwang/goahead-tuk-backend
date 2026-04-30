package io.goahead.tuk.confession.application.port

import io.goahead.tuk.confession.application.port.command.WriteConfessionCommand
import io.goahead.tuk.confession.application.port.command.WriteConfessionResponse

interface WriteConfessionUseCase {
    fun execute(command: WriteConfessionCommand) : WriteConfessionResponse
}