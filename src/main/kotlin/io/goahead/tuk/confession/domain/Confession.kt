package io.goahead.tuk.confession.domain

import io.goahead.tuk.confession.enums.ReactionType
import java.time.Instant

data class Confession (
    val id: ConfessionId,
    val authorId: AuthorId,
    val content: ConfessionContent,
    val reactions: ReactionCounts,
    val createdAt: Instant,
) {
    fun react(type: ReactionType): Confession {
        return copy(
            reactions = reactions.increase(type)
        )
    }

    companion object {
        fun write(
            id: ConfessionId,
            authorId: AuthorId,
            content: ConfessionContent,
            createdAt: Instant,
        ): Confession {
            return Confession(
                id = id,
                authorId = authorId,
                content = content,
                reactions = ReactionCounts.empty(),
                createdAt = createdAt,
            )
        }
    }
}