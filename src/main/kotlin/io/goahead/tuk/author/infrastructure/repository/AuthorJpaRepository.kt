package io.goahead.tuk.author.infrastructure.repository

import io.goahead.tuk.author.infrastructure.AuthorEntity
import org.springframework.data.jpa.repository.JpaRepository

// 엔티티가 internal이므로 레포지토리도 internal로 맞춰줍니다.
internal interface AuthorJpaRepository : JpaRepository<AuthorEntity, String> {
    // 필요한 경우 쿼리 메소드 추가
    // fun findBySomeField(value: String): AuthorEntity?
}