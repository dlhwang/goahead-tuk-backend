package io.goahead.tuk.confession.domain

import io.goahead.tuk.confession.enums.ReactionType
import io.kotest.property.Arb
import io.kotest.property.arbitrary.enum
import io.kotest.property.arbitrary.list
import io.kotest.property.checkAll
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
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
}
