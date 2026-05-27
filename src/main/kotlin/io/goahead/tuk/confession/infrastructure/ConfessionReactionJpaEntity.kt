package io.goahead.tuk.confession.infrastructure

import io.goahead.tuk.confession.enums.ReactionType
import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(
    name = "confession_reaction_selection",
    uniqueConstraints = [
        UniqueConstraint(
            name = "uk_confession_reaction_selection",
            columnNames = ["confession_id", "device_key", "reaction_type"]
        )
    ]
)
class ConfessionReactionJpaEntity(
    @Id
    @Column(name = "confession_reaction_id", nullable = false, updatable = false, length = 64)
    val id: String,

    @Column(name = "confession_id", nullable = false)
    val confessionId: String,

    @Column(name = "device_key", nullable = false, length = 64)
    val deviceKey: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "reaction_type", nullable = false, length = 50)
    val reactionType: ReactionType,

    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: Instant,
) {
    protected constructor() : this(
        id = "",
        confessionId = "",
        deviceKey = "",
        reactionType = ReactionType.PRAY,
        createdAt = Instant.EPOCH,
    )
}
