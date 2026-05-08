package io.goahead.tuk.confession.infrastructure.repository

import io.goahead.tuk.confession.enums.ReactionType
import io.goahead.tuk.confession.infrastructure.ConfessionReactionJpaEntity
import org.springframework.data.jpa.repository.JpaRepository

interface ConfessionReactionJpaRepository : JpaRepository<ConfessionReactionJpaEntity, String> {
    fun findByConfessionIdAndReactionType(
        confessionId: String,
        reactionType: ReactionType,
    ): ConfessionReactionJpaEntity?

    fun findAllByConfessionId(confessionId: String): List<ConfessionReactionJpaEntity>

    fun findAllByConfessionIdIn(confessionIds: Collection<String>): List<ConfessionReactionJpaEntity>
}
