package io.goahead.tuk.confession.api

import io.goahead.tuk.confession.api.request.WriteConfessionRequest
import io.goahead.tuk.confession.api.request.ReactConfessionRequest
import io.goahead.tuk.confession.api.response.ConfessionResponse
import io.goahead.tuk.confession.application.port.GetConfessionUseCase
import io.goahead.tuk.confession.application.port.ListConfessionsUseCase
import io.goahead.tuk.confession.application.port.ReactConfessionUseCase
import io.goahead.tuk.confession.application.port.WriteConfessionUseCase
import io.goahead.tuk.confession.application.port.command.ReactConfessionCommand
import io.goahead.tuk.confession.application.port.command.WriteConfessionCommand
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/confessions")
class ConfessionController(
    private val writeConfessionUseCase: WriteConfessionUseCase,
    private val getConfessionUseCase: GetConfessionUseCase,
    private val listConfessionsUseCase: ListConfessionsUseCase,
    private val reactConfessionUseCase: ReactConfessionUseCase,
) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun write(
        @RequestHeader("X-Device-Id") deviceId: String,
        @RequestBody request: WriteConfessionRequest,
    ): ConfessionResponse {
        val result = writeConfessionUseCase.execute(
            WriteConfessionCommand(
                authorId = deviceId,
                content = request.content,
            )
        )

        return ConfessionResponse.from(result)
    }

    @GetMapping("/{confessionId}")
    fun get(
        @PathVariable confessionId: String,
    ): ConfessionResponse {
        return ConfessionResponse.from(getConfessionUseCase.execute(confessionId))
    }

    @GetMapping
    fun list(): List<ConfessionResponse> {
        return listConfessionsUseCase.execute().map(ConfessionResponse::from)
    }

    @PostMapping("/{confessionId}/reactions")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun react(
        @PathVariable confessionId: String,
        @RequestBody request: ReactConfessionRequest,
    ) {
        reactConfessionUseCase.execute(
            ReactConfessionCommand(
                confessionId = confessionId,
                type = request.type,
            )
        )
    }
}
