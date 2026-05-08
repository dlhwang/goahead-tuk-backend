package io.goahead.tuk.author.domain

import java.time.LocalDateTime

data class Author(
    val id: AuthorId,
    val nickname: String,
    val createdAt: LocalDateTime
) {
    // 비즈니스 규칙: 생성된 지 30일이 지났는지 확인
    fun isExpired(): Boolean = createdAt.isBefore(LocalDateTime.now().minusDays(30))

    companion object {
        fun create(authorId: AuthorId, nickname: String): Author {

            return Author(
                id = authorId,
                nickname = nickname,
                createdAt = LocalDateTime.now()
            )
        }
    }
}
