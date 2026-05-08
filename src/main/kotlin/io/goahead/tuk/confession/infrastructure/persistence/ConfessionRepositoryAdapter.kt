package io.goahead.tuk.confession.infrastructure.persistence

import io.goahead.tuk.confession.domain.Confession
import io.goahead.tuk.confession.domain.ConfessionId
import io.goahead.tuk.confession.domain.ReactionCountItem
import io.goahead.tuk.confession.domain.ReactionCounts
import io.goahead.tuk.confession.domain.repository.ConfessionRepository
import io.goahead.tuk.confession.infrastructure.ConfessionJpaEntity
import io.goahead.tuk.confession.infrastructure.ConfessionReactionJpaEntity
import io.goahead.tuk.confession.infrastructure.repository.ConfessionJpaRepository
import io.goahead.tuk.confession.infrastructure.repository.ConfessionReactionJpaRepository
import org.springframework.stereotype.Repository

@Repository
internal class ConfessionRepositoryAdapter(
    private val jpaRepository: ConfessionJpaRepository,
    private val reactionJpaRepository: ConfessionReactionJpaRepository,
) : ConfessionRepository {
    override fun existsById(confessionId: ConfessionId): Boolean {
        return jpaRepository.existsById(confessionId.value)
    }

    override fun findById(confessionId: ConfessionId): Confession? {
        return jpaRepository.findById(confessionId.value)
            .map { entity ->
                entity.toDomain(
                    reactions = reactionCountsOf(
                        reactionJpaRepository.findAllByConfessionId(confessionId.value)
                    )
                )
            }
            .orElse(null)
    }

    override fun findAll(): List<Confession> {
        val confessions = jpaRepository.findAllByOrderByCreatedAtDesc()
        val reactionsByConfessionId = reactionJpaRepository
            .findAllByConfessionIdIn(confessions.map { it.id })
            .groupBy { it.confessionId }

        return confessions.map { entity ->
            entity.toDomain(
                reactions = reactionCountsOf(reactionsByConfessionId[entity.id].orEmpty())
            )
        }
    }

    override fun save(confession: Confession): Confession {
        return jpaRepository.save(ConfessionJpaEntity.from(confession)).toDomain()
    }

    private fun reactionCountsOf(reactions: List<ConfessionReactionJpaEntity>): ReactionCounts {
        return ReactionCounts.of(
            reactions.map {
                ReactionCountItem(
                    type = it.reactionType,
                    count = it.count,
                )
            }
        )
    }
}
