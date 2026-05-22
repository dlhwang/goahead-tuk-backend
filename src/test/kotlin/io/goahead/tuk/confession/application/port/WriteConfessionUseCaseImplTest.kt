package io.goahead.tuk.confession.application.port

import io.goahead.tuk.author.application.AuthorService
import io.goahead.tuk.author.domain.Author
import io.goahead.tuk.author.domain.AuthorId as DomainAuthorId
import io.goahead.tuk.author.domain.repository.AuthorRepository
import io.goahead.tuk.confession.application.ConfessionIdGenerator
import io.goahead.tuk.confession.domain.AuthorId as ConfessionAuthorId
import io.goahead.tuk.confession.domain.Confession
import io.goahead.tuk.confession.domain.ConfessionId
import io.goahead.tuk.confession.domain.repository.ConfessionRepository
import org.assertj.core.api.Assertions.assertThat
import java.time.LocalDateTime
import kotlin.test.Test

class WriteConfessionUseCaseImplTest {

    @Test
    fun `writes confession with generated id`() {
        val confessionRepository = FakeConfessionRepository()
        val service = WriteConfessionUseCaseImpl(
            authorService = AuthorService(FakeAuthorRepository(existingAuthorId = "author-1")),
            confessionIdGenerator = FixedConfessionIdGenerator(ConfessionId("confession-1")),
            confessionRepository = confessionRepository,
        )

        service.execute(authorId = "author-1", content = "test confession")

        val saved = confessionRepository.saved
        assertThat(saved).isNotNull
        assertThat(saved?.id).isEqualTo(ConfessionId("confession-1"))
        assertThat(saved?.authorId).isEqualTo(ConfessionAuthorId("author-1"))
        assertThat(saved?.content?.value).isEqualTo("test confession")
    }

    @Test
    fun `creates author when author does not exist`() {
        val confessionRepository = FakeConfessionRepository()
        val authorRepository = FakeAuthorRepository()
        val service = WriteConfessionUseCaseImpl(
            authorService = AuthorService(authorRepository),
            confessionIdGenerator = FixedConfessionIdGenerator(ConfessionId("confession-1")),
            confessionRepository = confessionRepository,
        )

        service.execute(authorId = "device-1", content = "test confession")

        assertThat(authorRepository.saved?.id).isEqualTo(DomainAuthorId("device-1"))
        assertThat(confessionRepository.saved?.authorId).isEqualTo(ConfessionAuthorId("device-1"))
    }

    private class FixedConfessionIdGenerator(
        private val confessionId: ConfessionId,
    ) : ConfessionIdGenerator {
        override fun generate(): ConfessionId = confessionId
    }

    private class FakeConfessionRepository : ConfessionRepository {
        var saved: Confession? = null

        override fun existsById(confessionId: ConfessionId): Boolean = saved?.id == confessionId

        override fun findById(confessionId: ConfessionId): Confession? = saved

        override fun findAll(): List<Confession> = listOfNotNull(saved)

        override fun save(confession: Confession): Confession {
            saved = confession
            return confession
        }
    }

    private class FakeAuthorRepository(
        private val existingAuthorId: String? = null,
    ) : AuthorRepository {
        var saved: Author? = null

        override fun findById(id: String): Author? {
            return when (id) {
                existingAuthorId -> Author(
                    id = DomainAuthorId(id),
                    nickname = "tester",
                    createdAt = LocalDateTime.now(),
                )

                else -> saved?.takeIf { it.id.value == id }
            }
        }

        override fun save(author: Author): Author {
            saved = author
            return author
        }
    }
}
