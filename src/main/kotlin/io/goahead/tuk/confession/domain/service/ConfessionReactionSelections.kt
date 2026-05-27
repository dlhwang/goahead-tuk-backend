package io.goahead.tuk.confession.domain.service

import io.goahead.tuk.confession.domain.ConfessionId
import io.goahead.tuk.confession.domain.ReactionCounts
import io.goahead.tuk.confession.enums.ReactionType

interface ConfessionReactionSelections {
    fun select(confessionId: ConfessionId, deviceKey: String, type: ReactionType)
    fun clear(confessionId: ConfessionId, deviceKey: String, type: ReactionType)
}

interface ReactionAggregateReader {
    fun aggregate(confessionId: ConfessionId): ReactionCounts
    fun aggregate(confessionIds: Collection<ConfessionId>): Map<ConfessionId, ReactionCounts>
}
