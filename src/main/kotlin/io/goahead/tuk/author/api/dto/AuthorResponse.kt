package io.goahead.tuk.author.api.dto

import io.goahead.tuk.author.domain.Author

data class AuthorResponse(
    val authorId: String,
    val nickname: String,
) {
    companion object {
        fun from(domain: Author): AuthorResponse {
            return AuthorResponse(
                authorId = domain.id.value,
                nickname = domain.nickname,
            )
        }
    }
}
