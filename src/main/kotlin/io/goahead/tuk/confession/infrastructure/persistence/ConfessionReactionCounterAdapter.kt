package io.goahead.tuk.confession.infrastructure.persistence

import io.goahead.tuk.common.IdGenerator
import io.goahead.tuk.confession.domain.ConfessionId
import io.goahead.tuk.confession.domain.service.ConfessionReactionCounter
import io.goahead.tuk.confession.enums.ReactionType
import io.goahead.tuk.confession.infrastructure.ConfessionReactionJpaEntity
import io.goahead.tuk.confession.infrastructure.repository.ConfessionReactionJpaRepository
import org.springframework.stereotype.Service

@Service
class ConfessionReactionCounterAdapter(
    private val idGenerator: IdGenerator,
    private val reactionJpaRepository: ConfessionReactionJpaRepository,
) : ConfessionReactionCounter {
    override fun increase(
        confessionId: ConfessionId,
        type: ReactionType
    ) {
        val reaction = reactionJpaRepository
            .findByConfessionIdAndReactionType(confessionId.value, type)

        val increased = if (reaction == null) {
            ConfessionReactionJpaEntity(
                id = idGenerator.generate(),
                confessionId = confessionId.value,
                reactionType = type,
                count = 1L,
            )
        } else {
            ConfessionReactionJpaEntity(
                id = reaction.id,
                confessionId = reaction.confessionId,
                reactionType = reaction.reactionType,
                count = reaction.count + 1,
            )
        }

        reactionJpaRepository.save(increased)
    }
}
