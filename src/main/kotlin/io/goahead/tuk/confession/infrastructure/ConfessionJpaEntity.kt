package io.goahead.tuk.confession.infrastructure

import io.goahead.tuk.confession.domain.AuthorId
import io.goahead.tuk.confession.domain.Confession
import io.goahead.tuk.confession.domain.ConfessionContent
import io.goahead.tuk.confession.domain.ConfessionId
import io.goahead.tuk.confession.domain.ReactionCounts
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.Instant


@Entity
@Table(name = "tb_confession")
internal class ConfessionJpaEntity(

    @Id
    @Column(name = "confession_id", nullable = false, updatable = false, length = 36)
    val id: String,

    @Column(name = "author_id", nullable = false, updatable = false, length = 36)
    val authorId: String,

    @Column(name = "content", nullable = false, length = 1000)
    val content: String,

    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: Instant,
) {
    protected constructor() : this(
        id = "",
        authorId = "",
        content = "",
        createdAt = Instant.EPOCH,
    )

    fun toDomain(reactions: ReactionCounts = ReactionCounts.empty()): Confession {
        return Confession(
            id = ConfessionId(id),
            authorId = AuthorId(authorId),
            content = ConfessionContent(content),
            reactions = reactions,
            createdAt = createdAt,
        )
    }

    companion object {
        fun from(confession: Confession): ConfessionJpaEntity {
            return ConfessionJpaEntity(
                id = confession.id.value,
                authorId = confession.authorId.value,
                content = confession.content.value,
                createdAt = confession.createdAt,
            )
        }
    }
}
