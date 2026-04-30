package io.goahead.tuk.author.domain.repository

import io.goahead.tuk.author.domain.Author

// 순수 도메인 인터페이스
interface AuthorRepository {
    fun findById(id: String): Author?
    fun save(author: Author): Author
}