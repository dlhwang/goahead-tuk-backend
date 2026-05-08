package io.goahead.tuk.author.infrastructure.persistence

import io.goahead.tuk.author.domain.Author
import io.goahead.tuk.author.domain.repository.AuthorRepository
import io.goahead.tuk.author.infrastructure.AuthorEntity
import io.goahead.tuk.author.infrastructure.repository.AuthorJpaRepository
import org.springframework.stereotype.Repository

@Repository
internal class AuthorRepositoryAdapter(
    private val jpaRepository: AuthorJpaRepository
) : AuthorRepository {

    override fun findById(id: String): Author? {
        return jpaRepository.findById(id).map { it.toModel() }.orElse(null)
    }

    override fun save(author: Author): Author {
        val entity = AuthorEntity(
            id = author.id.value,
            nickname = author.nickname,
            createdAt = author.createdAt,
        )
        return jpaRepository.save(entity).toModel()
    }
}
