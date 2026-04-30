package io.goahead.tuk.author.domain.repository

import io.goahead.tuk.author.infrastructure.AuthorEntity
import io.goahead.tuk.author.infrastructure.repository.AuthorJpaRepository
import io.goahead.tuk.author.domain.Author
import org.springframework.stereotype.Repository

@Repository
internal class AuthorRepositoryImpl(
    private val jpaRepository: AuthorJpaRepository
) : AuthorRepository { // 도메인 인터페이스 구현

    override fun findById(id: String): Author? {
        return jpaRepository.findById(id).map { it.toModel() }.orElse(null)
    }

    override fun save(author: Author): Author {
        // 도메인 모델을 다시 엔티티로 변환하여 저장 (toEntity 확장함수 필요)
        val entity = AuthorEntity(id = author.id.value, createdAt = author.createdAt)
        return jpaRepository.save(entity).toModel()
    }
}