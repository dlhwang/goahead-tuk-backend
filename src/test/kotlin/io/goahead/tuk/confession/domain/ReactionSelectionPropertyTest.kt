package io.goahead.tuk.confession.domain

import io.goahead.tuk.confession.application.port.command.GetConfessionResponse
import io.goahead.tuk.confession.enums.ReactionType
import io.kotest.property.Arb
import io.kotest.property.arbitrary.enum
import io.kotest.property.arbitrary.list
import io.kotest.property.checkAll
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import java.time.Instant
import kotlin.test.Test

class ReactionSelectionPropertyTest {
    @Test
    fun `selection set count matches idempotent command model`() {
        runBlocking {
            checkAll(Arb.list(Arb.enum<ReactionType>(), range = 0..100)) { selections ->
                val active = mutableSetOf<ReactionType>()
                selections.forEach { active.add(it) }

                assertThat(active.size).isEqualTo(selections.distinct().size)
                assertThat(active).isSubsetOf(ReactionType.entries)
            }
        }
    }

    @Test
    fun `selected by me matches requester selection set for every reaction type`() {
        runBlocking {
            checkAll(Arb.list(Arb.enum<ReactionType>(), range = 0..100)) { selections ->
                val active = selections.toSet()
                val result = GetConfessionResponse.from(confession(), active)

                result.reactions.forEach { reaction ->
                    assertThat(reaction.selectedByMe).isEqualTo(reaction.type in active)
                }
            }
        }
    }

    private fun confession(): Confession {
        return Confession(
            id = ConfessionId("confession-1"),
            authorId = AuthorId("author-1"),
            content = ConfessionContent("content"),
            reactions = ReactionCounts.empty(),
            createdAt = Instant.EPOCH,
        )
    }
}
