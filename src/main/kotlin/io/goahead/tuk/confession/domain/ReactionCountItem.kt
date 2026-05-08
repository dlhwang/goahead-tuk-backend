package io.goahead.tuk.confession.domain

import io.goahead.tuk.confession.enums.ReactionType

data class ReactionCountItem(
    val type: ReactionType,
    val count: Long
)