package io.goahead.tuk.confession.api

import io.goahead.tuk.confession.api.request.WriteConfessionRequest
import io.goahead.tuk.confession.api.response.ConfessionResponse
import io.goahead.tuk.confession.application.port.GetConfessionUseCase
import io.goahead.tuk.confession.application.port.ListConfessionsUseCase
import io.goahead.tuk.confession.application.port.ReactConfessionUseCase
import io.goahead.tuk.confession.application.port.WriteConfessionUseCase
import io.goahead.tuk.confession.application.port.command.ReactConfessionCommand
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.DeleteMapping
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
    private val log = LoggerFactory.getLogger(javaClass)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun write(
        @RequestHeader("X-Device-Id") deviceId: String,
        @RequestBody request: WriteConfessionRequest,
    ): ConfessionResponse {
        validateDeviceId(deviceId)
        log.info("Writing confession")

        val result = writeConfessionUseCase.execute(deviceId, request.content)

        log.info("Written confession id={}", result.id)
        return ConfessionResponse.from(result)
    }

    @GetMapping("/{confessionId}")
    fun get(
        @PathVariable confessionId: String,
    ): ConfessionResponse {
        log.info("Getting confession id={}", confessionId)

        val result = getConfessionUseCase.execute(confessionId)

        log.info("Got confession id={}", result.id)
        return ConfessionResponse.from(result)
    }

    @GetMapping
    fun list(): List<ConfessionResponse> {
        log.info("Listing confessions")

        val results = listConfessionsUseCase.execute()

        log.info("Listed confessions count={}", results.size)
        return results.map(ConfessionResponse::from)
    }

    @PutMapping("/{confessionId}/reactions/{type}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun selectReaction(
        @PathVariable confessionId: String,
        @PathVariable type: String,
        @RequestHeader("X-Device-Id") deviceId: String,
    ) {
        validateDeviceId(deviceId)
        reactConfessionUseCase.select(
            ReactConfessionCommand(
                confessionId = confessionId,
                deviceId = deviceId,
                type = type,
            )
        )
    }

    @DeleteMapping("/{confessionId}/reactions/{type}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun clearReaction(
        @PathVariable confessionId: String,
        @PathVariable type: String,
        @RequestHeader("X-Device-Id") deviceId: String,
    ) {
        validateDeviceId(deviceId)
        reactConfessionUseCase.clear(
            ReactConfessionCommand(
                confessionId = confessionId,
                deviceId = deviceId,
                type = type,
            )
        )
    }

    private fun validateDeviceId(deviceId: String) {
        require(DEVICE_ID_PATTERN.matches(deviceId)) { "Invalid device identifier" }
    }

    companion object {
        private val DEVICE_ID_PATTERN = Regex("[A-Za-z0-9_-]{1,128}")
    }
}
