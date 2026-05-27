package io.goahead.tuk.confession.application.port

import io.goahead.tuk.common.security.DeviceKeyHasher
import io.goahead.tuk.confession.domain.AuthorId
import io.goahead.tuk.confession.domain.Confession
import io.goahead.tuk.confession.domain.ConfessionContent
import io.goahead.tuk.confession.domain.ConfessionId
import io.goahead.tuk.confession.domain.ReactionCountItem
import io.goahead.tuk.confession.domain.ReactionCounts
import io.goahead.tuk.confession.domain.repository.ConfessionRepository
import io.goahead.tuk.confession.domain.service.ReactionAggregateReader
import io.goahead.tuk.confession.domain.service.ReactionSelectionReader
import io.goahead.tuk.confession.enums.ReactionType
import io.goahead.tuk.confession.exception.ConfessionNotFoundException
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import java.time.Instant
import kotlin.test.Test

class GetConfessionUseCaseImplTest {

    @Test
    fun `gets confession by id`() {
        val service = GetConfessionUseCaseImpl(
            confessionRepository = FakeConfessionRepository(
                confessions = listOf(confession("confession-1"))
            ),
            reactionAggregateReader = FakeReactionAggregateReader(
                ReactionCounts.of(
                    listOf(
                        ReactionCountItem(ReactionType.PRAY, 3),
                        ReactionCountItem(ReactionType.COMFORT, 2),
                    )
                )
            ),
            reactionSelectionReader = FakeReactionSelectionReader(setOf(ReactionType.PRAY, ReactionType.COMFORT)),
            deviceKeyHasher = FakeDeviceKeyHasher(),
        )

        val result = service.execute("confession-1", "reader-1")

        assertThat(result.id).isEqualTo("confession-1")
        assertThat(result.content).isEqualTo("content")
        assertThat(result.reactions.associate { it.type to it.selectedByMe }).containsExactlyInAnyOrderEntriesOf(
            mapOf(
                ReactionType.PRAY to true,
                ReactionType.COMFORT to true,
                ReactionType.TOGETHER to false,
            )
        )
        assertThat(result.reactions.associate { it.type to it.count }).containsEntry(ReactionType.PRAY, 3)
    }

    @Test
    fun `throws when confession is missing`() {
        val service = GetConfessionUseCaseImpl(
            confessionRepository = FakeConfessionRepository(confessions = emptyList()),
            reactionAggregateReader = FakeReactionAggregateReader(),
            reactionSelectionReader = FakeReactionSelectionReader(),
            deviceKeyHasher = FakeDeviceKeyHasher(),
        )

        assertThatThrownBy {
            service.execute("missing", "reader-1")
        }.isInstanceOf(ConfessionNotFoundException::class.java)
    }

    @Test
    fun `reads selection using hashed requester identity`() {
        val selections = FakeReactionSelectionReader()
        val service = GetConfessionUseCaseImpl(
            confessionRepository = FakeConfessionRepository(listOf(confession("confession-1"))),
            reactionAggregateReader = FakeReactionAggregateReader(),
            reactionSelectionReader = selections,
            deviceKeyHasher = FakeDeviceKeyHasher(),
        )

        service.execute("confession-1", "reader-1")

        assertThat(selections.requestedDeviceKey).isEqualTo("hashed-reader-1")
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

    private class FakeReactionAggregateReader(
        private val counts: ReactionCounts = ReactionCounts.empty(),
    ) : ReactionAggregateReader {
        override fun aggregate(confessionId: ConfessionId): ReactionCounts = counts
        override fun aggregate(confessionIds: Collection<ConfessionId>): Map<ConfessionId, ReactionCounts> = emptyMap()
    }

    private class FakeReactionSelectionReader(
        private val selectedTypes: Set<ReactionType> = emptySet(),
    ) : ReactionSelectionReader {
        var requestedDeviceKey: String? = null

        override fun selectedTypes(confessionId: ConfessionId, deviceKey: String): Set<ReactionType> {
            requestedDeviceKey = deviceKey
            return selectedTypes
        }
    }

    private class FakeDeviceKeyHasher : DeviceKeyHasher {
        override fun hash(deviceId: String): String = "hashed-$deviceId"
    }
}

fun confession(id: String): Confession {
    return Confession(
        id = ConfessionId(id),
        authorId = AuthorId("author-1"),
        content = ConfessionContent("content"),
        reactions = ReactionCounts.empty(),
        createdAt = Instant.parse("2026-05-08T00:00:00Z"),
    )
}
