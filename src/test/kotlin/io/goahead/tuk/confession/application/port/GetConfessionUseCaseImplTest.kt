package io.goahead.tuk.confession.application.port

import io.goahead.tuk.confession.domain.AuthorId
import io.goahead.tuk.confession.domain.Confession
import io.goahead.tuk.confession.domain.ConfessionContent
import io.goahead.tuk.confession.domain.ConfessionId
import io.goahead.tuk.confession.domain.ReactionCounts
import io.goahead.tuk.confession.domain.repository.ConfessionRepository
import io.goahead.tuk.confession.exception.ConfessionNotFoundException
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import java.time.Instant
import kotlin.test.Test

class GetConfessionUseCaseImplTest {

    @Test
    fun `gets confession by id`() {
        val service = GetConfessionUseCaseImpl(
            confessionRepository = FakeConfessionRepository(
                confessions = listOf(confession("confession-1"))
            )
        )

        val result = service.execute("confession-1")

        assertThat(result.id).isEqualTo("confession-1")
        assertThat(result.authorId).isEqualTo("author-1")
        assertThat(result.content).isEqualTo("content")
    }

    @Test
    fun `throws when confession is missing`() {
        val service = GetConfessionUseCaseImpl(
            confessionRepository = FakeConfessionRepository(confessions = emptyList())
        )

        assertThatThrownBy {
            service.execute("missing")
        }.isInstanceOf(ConfessionNotFoundException::class.java)
    }

    private class FakeConfessionRepository(
        private val confessions: List<Confession>,
    ) : ConfessionRepository {
        override fun existsById(confessionId: ConfessionId): Boolean {
            return confessions.any { it.id == confessionId }
        }

        override fun findById(confessionId: ConfessionId): Confession? {
            return confessions.firstOrNull { it.id == confessionId }
        }

        override fun findAll(): List<Confession> = confessions

        override fun save(confession: Confession): Confession = confession
    }
}

fun confession(id: String): Confession {
    return Confession(
        id = ConfessionId(id),
        authorId = AuthorId("author-1"),
        content = ConfessionContent("content"),
        reactions = ReactionCounts.empty(),
        createdAt = Instant.parse("2026-05-08T00:00:00Z"),
    )
}
