package io.goahead.tuk.confession.domain.service

import io.goahead.tuk.confession.domain.ConfessionId
import io.goahead.tuk.confession.enums.ReactionType

interface ConfessionReactionCounter {
    fun increase(confessionId: ConfessionId, type: ReactionType)
}
