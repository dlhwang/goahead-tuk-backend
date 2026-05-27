package io.goahead.tuk.confession.api.response

import io.goahead.tuk.confession.application.port.command.WriteConfessionResponse
import io.goahead.tuk.confession.application.port.command.GetConfessionResponse
import io.goahead.tuk.confession.enums.ReactionType
import java.time.Instant

data class ConfessionResponse(
    val id: String,
    val content: String,
    val createdAt: Instant,
    val reactions: List<ReactionCountResponse>,
) {
    companion object {
        fun from(result: WriteConfessionResponse): ConfessionResponse {
            return ConfessionResponse(
                id = result.id,
                content = result.content,
                createdAt = result.createdAt,
                reactions = result.reactions.map {
                    ReactionCountResponse(
                        type = it.type,
                        count = it.count,
                    )
                },
            )
        }
    }
}

data class ReactionCountResponse(
    val type: ReactionType,
    val count: Long,
)

data class ConfessionDetailResponse(
    val id: String,
    val content: String,
    val createdAt: Instant,
    val reactions: List<SelectedReactionCountResponse>,
) {
    companion object {
        fun from(result: GetConfessionResponse): ConfessionDetailResponse {
            return ConfessionDetailResponse(
                id = result.id,
                content = result.content,
                createdAt = result.createdAt,
                reactions = result.reactions.map {
                    SelectedReactionCountResponse(
                        type = it.type,
                        count = it.count,
                        selectedByMe = it.selectedByMe,
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
