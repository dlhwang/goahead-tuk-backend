package io.goahead.tuk.confession.application.port

import io.goahead.tuk.confession.application.port.command.WriteConfessionResponse

interface ListConfessionsUseCase {
    fun execute(): List<WriteConfessionResponse>
}
