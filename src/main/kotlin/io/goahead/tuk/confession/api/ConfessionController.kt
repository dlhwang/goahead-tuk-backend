package io.goahead.tuk.confession.api

import io.goahead.tuk.confession.api.request.WriteConfessionRequest
import io.goahead.tuk.confession.application.port.WriteConfessionUseCase
import io.goahead.tuk.confession.application.port.command.WriteConfessionCommand
import io.goahead.tuk.confession.application.port.command.WriteConfessionResponse
import io.goahead.tuk.confession.domain.AuthorId
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/confessions")
class ConfessionController(
    private val writeConfessionUseCase: WriteConfessionUseCase,
) {

    @PostMapping
    fun write(
        @RequestHeader("X-Device-Id") deviceId: String,
        @RequestBody request: WriteConfessionRequest,
    ): WriteConfessionResponse {
        val result = writeConfessionUseCase.execute(
            WriteConfessionCommand(
                authorId = AuthorId(deviceId),
                content = request.content,
            )
        )

        return WriteConfessionResponse.from(result)
    }
}