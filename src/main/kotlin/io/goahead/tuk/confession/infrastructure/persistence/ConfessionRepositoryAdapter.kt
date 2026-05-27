package io.goahead.tuk.confession.infrastructure.persistence

import io.goahead.tuk.confession.domain.Confession
import io.goahead.tuk.confession.domain.ConfessionId
import io.goahead.tuk.confession.domain.repository.ConfessionRepository
import io.goahead.tuk.confession.infrastructure.ConfessionJpaEntity
import io.goahead.tuk.confession.infrastructure.repository.ConfessionJpaRepository
import org.springframework.stereotype.Repository

@Repository
internal class ConfessionRepositoryAdapter(
    private val jpaRepository: ConfessionJpaRepository,
) : ConfessionRepository {
    override fun existsById(confessionId: ConfessionId): Boolean {
        return jpaRepository.existsById(confessionId.value)
    }

    override fun findById(confessionId: ConfessionId): Confession? {
        return jpaRepository.findById(confessionId.value)
            .map { entity -> entity.toDomain() }
            .orElse(null)
    }

    override fun findAll(): List<Confession> {
        return jpaRepository.findAllByOrderByCreatedAtDesc().map { it.toDomain() }
    }

    override fun save(confession: Confession): Confession {
        return jpaRepository.save(ConfessionJpaEntity.from(confession)).toDomain()
    }

}
