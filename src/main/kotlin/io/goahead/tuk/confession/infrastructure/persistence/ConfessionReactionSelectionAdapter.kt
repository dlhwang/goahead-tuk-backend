package io.goahead.tuk.confession.infrastructure.persistence

import io.goahead.tuk.common.IdGenerator
import io.goahead.tuk.confession.domain.ConfessionId
import io.goahead.tuk.confession.domain.ReactionCountItem
import io.goahead.tuk.confession.domain.ReactionCounts
import io.goahead.tuk.confession.domain.service.ConfessionReactionSelections
import io.goahead.tuk.confession.domain.service.ReactionAggregateReader
import io.goahead.tuk.confession.domain.service.ReactionSelectionReader
import io.goahead.tuk.confession.enums.ReactionType
import io.goahead.tuk.confession.infrastructure.ConfessionReactionJpaEntity
import io.goahead.tuk.confession.infrastructure.repository.ConfessionReactionJpaRepository
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.stereotype.Repository
import java.time.Instant

@Repository
class ConfessionReactionSelectionAdapter(
    private val idGenerator: IdGenerator,
    private val repository: ConfessionReactionJpaRepository,
) : ConfessionReactionSelections, ReactionAggregateReader, ReactionSelectionReader {
    override fun select(confessionId: ConfessionId, deviceKey: String, type: ReactionType) {
        if (repository.existsByConfessionIdAndDeviceKeyAndReactionType(confessionId.value, deviceKey, type)) {
            return
        }

        try {
            repository.saveAndFlush(
                ConfessionReactionJpaEntity(
                    id = idGenerator.generate(),
                    confessionId = confessionId.value,
                    deviceKey = deviceKey,
                    reactionType = type,
                    createdAt = Instant.now(),
                )
            )
        } catch (_: DataIntegrityViolationException) {
            // A racing identical selection has already produced the requested state.
        }
    }

    override fun clear(confessionId: ConfessionId, deviceKey: String, type: ReactionType) {
        repository.deleteByConfessionIdAndDeviceKeyAndReactionType(confessionId.value, deviceKey, type)
    }

    override fun aggregate(confessionId: ConfessionId): ReactionCounts {
        return ReactionCounts.of(
            repository.aggregateByConfessionId(confessionId.value).map {
                ReactionCountItem(type = it.reactionType, count = it.count)
            }
        )
    }

    override fun aggregate(confessionIds: Collection<ConfessionId>): Map<ConfessionId, ReactionCounts> {
        if (confessionIds.isEmpty()) {
            return emptyMap()
        }

        return repository.aggregateByConfessionIdIn(confessionIds.map { it.value })
            .groupBy { ConfessionId(it.confessionId) }
            .mapValues { (_, values) ->
                ReactionCounts.of(values.map { ReactionCountItem(it.reactionType, it.count) })
            }
    }

    override fun selectedTypes(confessionId: ConfessionId, deviceKey: String): Set<ReactionType> {
        return repository.findReactionTypesByConfessionIdAndDeviceKey(confessionId.value, deviceKey).toSet()
    }
}
