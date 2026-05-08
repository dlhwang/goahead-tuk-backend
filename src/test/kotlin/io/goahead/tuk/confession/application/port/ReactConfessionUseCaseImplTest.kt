package io.goahead.tuk.confession.application.port

import io.goahead.tuk.confession.application.port.command.ReactConfessionCommand
import io.goahead.tuk.confession.domain.Confession
import io.goahead.tuk.confession.domain.ConfessionId
import io.goahead.tuk.confession.domain.repository.ConfessionRepository
import io.goahead.tuk.confession.domain.service.ConfessionReactionCounter
import io.goahead.tuk.confession.enums.ReactionType
import io.goahead.tuk.confession.exception.ConfessionNotFoundException
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import kotlin.test.Test

class ReactConfessionUseCaseImplTest {

    @Test
    fun `increases reaction when confession exists`() {
        val reactionCounter = FakeConfessionReactionCounter()
        val service = ReactConfessionUseCaseImpl(
            confessionRepository = FakeConfessionRepository(existing = true),
            reactionCounter = reactionCounter,
        )

        service.execute(
            ReactConfessionCommand(
                confessionId = "confession-1",
                type = ReactionType.EMPATHY.name,
            )
        )

        assertThat(reactionCounter.increasedConfessionId).isEqualTo(ConfessionId("confession-1"))
        assertThat(reactionCounter.increasedType).isEqualTo(ReactionType.EMPATHY)
    }

    @Test
    fun `throws when confession does not exist`() {
        val reactionCounter = FakeConfessionReactionCounter()
        val service = ReactConfessionUseCaseImpl(
            confessionRepository = FakeConfessionRepository(existing = false),
            reactionCounter = reactionCounter,
        )

        assertThatThrownBy {
            service.execute(
                ReactConfessionCommand(
                    confessionId = "missing-confession",
                    type = ReactionType.SUPPORT.name,
                )
            )
        }.isInstanceOf(ConfessionNotFoundException::class.java)

        assertThat(reactionCounter.increasedConfessionId).isNull()
    }

    private class FakeConfessionRepository(
        private val existing: Boolean,
    ) : ConfessionRepository {
        override fun existsById(confessionId: ConfessionId): Boolean = existing

        override fun findById(confessionId: ConfessionId): Confession? = null

        override fun findAll(): List<Confession> = emptyList()

        override fun save(confession: Confession): Confession = confession
    }

    private class FakeConfessionReactionCounter : ConfessionReactionCounter {
        var increasedConfessionId: ConfessionId? = null
        var increasedType: ReactionType? = null

        override fun increase(confessionId: ConfessionId, type: ReactionType) {
            increasedConfessionId = confessionId
            increasedType = type
        }
    }
}
