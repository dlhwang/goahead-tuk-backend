package io.goahead.tuk.confession.application.port

import io.goahead.tuk.confession.application.port.command.GetConfessionResponse

interface GetConfessionUseCase {
    fun execute(confessionId: String, deviceId: String): GetConfessionResponse
}
