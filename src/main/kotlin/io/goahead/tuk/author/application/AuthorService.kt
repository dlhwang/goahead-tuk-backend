package io.goahead.tuk.author.application

import io.goahead.tuk.author.application.command.SaveAuthorCommand
import io.goahead.tuk.author.domain.Author
import io.goahead.tuk.author.domain.AuthorId
import io.goahead.tuk.author.domain.repository.AuthorRepository
import io.goahead.tuk.author.exception.AuthorNotFoundException
import org.springframework.stereotype.Service

@Service
class AuthorService(
    private val authorRepository: AuthorRepository // Infrastructure의 JpaRepository 의존
) {
    fun getAuthor(id: String): Author {
        val author = authorRepository.findById(id)
            ?: throw AuthorNotFoundException("존재하지 않는 영혼입니다: $id")

        // 엔티티의 확장 함수를 이용해 도메인 모델로 변환하여 반환
        return author
    }

    fun save(command: SaveAuthorCommand): Author {
        val author = Author.create(
            AuthorId(command.authorId), command.nickname
        )

        return authorRepository.save(author)
    }
}