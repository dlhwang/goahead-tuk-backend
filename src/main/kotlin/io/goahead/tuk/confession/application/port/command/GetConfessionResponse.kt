package io.goahead.tuk.confession.application.port.command

import io.goahead.tuk.confession.domain.Confession
import io.goahead.tuk.confession.enums.ReactionType
import java.time.Instant

data class GetConfessionResponse(
    val id: String,
    val content: String,
    val createdAt: Instant,
    val reactions: List<SelectedReactionCountResponse>,
) {
    companion object {
        fun from(confession: Confession, selectedTypes: Set<ReactionType>): GetConfessionResponse {
            return GetConfessionResponse(
                id = confession.id.value,
                content = confession.content.value,
                createdAt = confession.createdAt,
                reactions = confession.reactions.toList().map {
                    SelectedReactionCountResponse(
                        type = it.type,
                        count = it.count,
                        selectedByMe = it.type in selectedTypes,
                    )
                },
            )
        }
    }
}

data class SelectedReactionCountResponse(
    val type: ReactionType,
    val count: Long,
    val selectedByMe: Boolean,
)
