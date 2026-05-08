package io.goahead.tuk.confession.domain

import io.goahead.tuk.confession.enums.ReactionType

class ReactionCounts private constructor(
    private val counts: Map<ReactionType, ReactionCount>
) {
    fun increase(type: ReactionType): ReactionCounts {
        val current = counts[type] ?: ReactionCount.ZERO

        return ReactionCounts(
            counts = counts + (type to current.increase())
        )
    }

    fun countOf(type: ReactionType): Long {
        return counts[type]?.value ?: 0L
    }

    fun toList(): List<ReactionCountItem> {
        return ReactionType.entries.map { type ->
            ReactionCountItem(
                type = type,
                count = countOf(type)
            )
        }
    }

    companion object {
        fun empty(): ReactionCounts {
            return ReactionCounts(emptyMap())
        }

        fun of(items: List<ReactionCountItem>): ReactionCounts {
            require(items.distinctBy { it.type }.size == items.size) {
                "반응 타입이 중복될 수 없습니다."
            }

            return ReactionCounts(
                items.associate { it.type to ReactionCount.of(it.count) }
            )
        }
    }
}