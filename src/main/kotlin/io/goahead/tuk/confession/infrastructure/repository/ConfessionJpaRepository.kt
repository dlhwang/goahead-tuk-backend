package io.goahead.tuk.confession.infrastructure.repository

import io.goahead.tuk.confession.infrastructure.ConfessionJpaEntity
import org.springframework.data.jpa.repository.JpaRepository

internal interface ConfessionJpaRepository : JpaRepository<ConfessionJpaEntity, String> {
    fun findAllByOrderByCreatedAtDesc(): List<ConfessionJpaEntity>
}
