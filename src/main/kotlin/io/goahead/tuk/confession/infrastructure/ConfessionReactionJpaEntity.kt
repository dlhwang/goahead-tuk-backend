package io.goahead.tuk.confession.infrastructure

import io.goahead.tuk.confession.enums.ReactionType
import jakarta.persistence.*

@Entity
@Table(
    name = "confession_reaction",
    uniqueConstraints = [
        UniqueConstraint(
            name = "uk_confession_reaction_confession_type",
            columnNames = ["confession_id", "reaction_type"]
        )
    ]
)
class ConfessionReactionJpaEntity(
    @Id
    @Column(name = "confession_reaction_id", nullable = false, updatable = false, length = 64)
    val id: String,

    @Column(name = "confession_id", nullable = false)
    val confessionId: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "reaction_type", nullable = false, length = 50)
    val reactionType: ReactionType,

    @Column(name = "reaction_count", nullable = false)
    val count: Long,
) {
    protected constructor() : this(
        id = "",
        confessionId = "",
        reactionType = ReactionType.EMPATHY,
        count = 0L
    )
}