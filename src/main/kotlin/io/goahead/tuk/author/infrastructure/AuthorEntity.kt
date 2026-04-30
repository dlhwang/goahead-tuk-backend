package io.goahead.tuk.author.infrastructure

import com.fasterxml.uuid.Generators
import io.goahead.tuk.author.domain.Author
import io.goahead.tuk.author.domain.AuthorId
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "tb_authors")
internal class AuthorEntity( // internal로 캡슐화
    @Id val id: String = Generators.timeBasedEpochGenerator().generate().toString(),
    val createdAt: LocalDateTime = LocalDateTime.now()
) {
    // Entity를 Domain Model로 변환하는 확장 함수
    fun toModel() = Author(id = AuthorId(id), createdAt = createdAt)
}