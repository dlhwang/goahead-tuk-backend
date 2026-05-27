package io.goahead.tuk.confession.application.port

import io.goahead.tuk.confession.domain.Confession
import io.goahead.tuk.confession.domain.ConfessionId
import io.goahead.tuk.confession.domain.ReactionCounts
import io.goahead.tuk.confession.domain.repository.ConfessionRepository
import io.goahead.tuk.confession.domain.service.ReactionAggregateReader
import org.assertj.core.api.Assertions.assertThat
import kotlin.test.Test

class ListConfessionsUseCaseImplTest {

    @Test
    fun `lists confessions`() {
        val service = ListConfessionsUseCaseImpl(
            confessionRepository = FakeConfessionRepository(
                confessions = listOf(confession("confession-1"), confession("confession-2"))
            ),
            reactionAggregateReader = FakeReactionAggregateReader(),
        )

        val results = service.execute()

        assertThat(results).extracting("id").containsExactly("confession-1", "confession-2")
    }

    private class FakeConfessionRepository(
        private val confessions: List<Confession>,
    ) : ConfessionRepository {
        override fun existsById(confessionId: ConfessionId): Boolean {
            return confessions.any { it.id == confessionId }
        }

        override fun findById(confessionId: ConfessionId): Confession? {
            return confessions.firstOrNull { it.id == confessionId }
        }

        override fun findAll(): List<Confession> = confessions

        override fun save(confession: Confession): Confession = confession
    }

    private class FakeReactionAggregateReader : ReactionAggregateReader {
        override fun aggregate(confessionId: ConfessionId): ReactionCounts = ReactionCounts.empty()
        override fun aggregate(confessionIds: Collection<ConfessionId>): Map<ConfessionId, ReactionCounts> = emptyMap()
    }
}
