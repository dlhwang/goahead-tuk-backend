package io.goahead.tuk.confession.api

import io.goahead.tuk.confession.application.port.GetConfessionUseCase
import io.goahead.tuk.confession.application.port.ListConfessionsUseCase
import io.goahead.tuk.confession.application.port.ReactConfessionUseCase
import io.goahead.tuk.confession.application.port.WriteConfessionUseCase
import io.goahead.tuk.confession.application.port.command.GetConfessionResponse
import io.goahead.tuk.confession.application.port.command.ReactConfessionCommand
import io.goahead.tuk.confession.application.port.command.SelectedReactionCountResponse
import io.goahead.tuk.confession.application.port.command.WriteConfessionResponse
import io.goahead.tuk.confession.enums.ReactionType
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import java.time.Instant
import kotlin.test.Test

class ConfessionControllerTest {

    @Test
    fun `passes device id to single confession lookup`() {
        val getUseCase = FakeGetConfessionUseCase()
        val controller = controller(getUseCase)

        val response = controller.get("confession-1", "reader-1")

        assertThat(getUseCase.deviceId).isEqualTo("reader-1")
        assertThat(response.reactions.single().selectedByMe).isTrue()
    }

    @Test
    fun `rejects invalid device id before single confession lookup`() {
        val getUseCase = FakeGetConfessionUseCase()
        val controller = controller(getUseCase)

        assertThatThrownBy {
            controller.get("confession-1", "invalid device")
        }.isInstanceOf(IllegalArgumentException::class.java)
        assertThat(getUseCase.deviceId).isNull()
    }

    private fun controller(getUseCase: FakeGetConfessionUseCase): ConfessionController {
        return ConfessionController(
            writeConfessionUseCase = FakeWriteConfessionUseCase(),
            getConfessionUseCase = getUseCase,
            listConfessionsUseCase = FakeListConfessionsUseCase(),
            reactConfessionUseCase = FakeReactConfessionUseCase(),
        )
    }

    private class FakeGetConfessionUseCase : GetConfessionUseCase {
        var deviceId: String? = null

        override fun execute(confessionId: String, deviceId: String): GetConfessionResponse {
            this.deviceId = deviceId
            return GetConfessionResponse(
                id = confessionId,
                content = "content",
                createdAt = Instant.EPOCH,
                reactions = listOf(SelectedReactionCountResponse(ReactionType.PRAY, 1, true)),
            )
        }
    }

    private class FakeWriteConfessionUseCase : WriteConfessionUseCase {
        override fun execute(authorId: String, content: String): WriteConfessionResponse {
            error("Not called")
        }
    }

    private class FakeListConfessionsUseCase : ListConfessionsUseCase {
        override fun execute(): List<WriteConfessionResponse> = emptyList()
    }

    private class FakeReactConfessionUseCase : ReactConfessionUseCase {
        override fun select(command: ReactConfessionCommand) = Unit
        override fun clear(command: ReactConfessionCommand) = Unit
    }
}
