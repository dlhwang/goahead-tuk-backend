package io.goahead.tuk.confession.infrastructure.repository

import io.goahead.tuk.confession.enums.ReactionType
import io.goahead.tuk.confession.infrastructure.ConfessionReactionJpaEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface ConfessionReactionJpaRepository : JpaRepository<ConfessionReactionJpaEntity, String> {
    fun existsByConfessionIdAndDeviceKeyAndReactionType(
        confessionId: String,
        deviceKey: String,
        reactionType: ReactionType,
    ): Boolean

    fun deleteByConfessionIdAndDeviceKeyAndReactionType(
        confessionId: String,
        deviceKey: String,
        reactionType: ReactionType,
    )

    @Query(
        """
        select r.reactionType as reactionType, count(r) as count
        from ConfessionReactionJpaEntity r
        where r.confessionId = :confessionId
        group by r.reactionType
        """
    )
    fun aggregateByConfessionId(@Param("confessionId") confessionId: String): List<ReactionAggregate>

    @Query(
        """
        select r.confessionId as confessionId, r.reactionType as reactionType, count(r) as count
        from ConfessionReactionJpaEntity r
        where r.confessionId in :confessionIds
        group by r.confessionId, r.reactionType
        """
    )
    fun aggregateByConfessionIdIn(
        @Param("confessionIds") confessionIds: Collection<String>
    ): List<ConfessionReactionAggregate>
}

interface ReactionAggregate {
    val reactionType: ReactionType
    val count: Long
}

interface ConfessionReactionAggregate {
    val confessionId: String
    val reactionType: ReactionType
    val count: Long
}
