package io.goahead.tuk.confession.application.port

import io.goahead.tuk.confession.application.port.command.WriteConfessionResponse

interface GetConfessionUseCase {
    fun execute(confessionId: String): WriteConfessionResponse
}
