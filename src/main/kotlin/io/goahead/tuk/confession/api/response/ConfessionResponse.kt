package io.goahead.tuk.confession.api.response

import io.goahead.tuk.confession.application.port.command.WriteConfessionResponse
import io.goahead.tuk.confession.enums.ReactionType
import java.time.Instant

data class ConfessionResponse(
    val id: String,
    val authorId: String,
    val content: String,
    val createdAt: Instant,
    val reactions: List<ReactionCountResponse>,
) {
    companion object {
        fun from(result: WriteConfessionResponse): ConfessionResponse {
            return ConfessionResponse(
                id = result.id,
                authorId = result.authorId,
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
