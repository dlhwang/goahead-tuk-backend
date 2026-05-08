package io.goahead.tuk.confession.application.port

import io.goahead.tuk.author.application.AuthorService
import io.goahead.tuk.confession.application.ConfessionIdGenerator
import io.goahead.tuk.confession.application.port.command.WriteConfessionCommand
import io.goahead.tuk.confession.application.port.command.WriteConfessionResponse
import io.goahead.tuk.confession.domain.AuthorId
import io.goahead.tuk.confession.domain.Confession
import io.goahead.tuk.confession.domain.ConfessionContent
import io.goahead.tuk.confession.domain.repository.ConfessionRepository
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class WriteConfessionUseCaseImpl(
    private val authorService: AuthorService,
    private val confessionIdGenerator: ConfessionIdGenerator,
    private val confessionRepository: ConfessionRepository,
) : WriteConfessionUseCase {

    override fun execute(command: WriteConfessionCommand): WriteConfessionResponse {
        val author = authorService.getOrCreateAuthor(command.authorId)
        val authorId = AuthorId(author.id.value)
        val confessionId = confessionIdGenerator.generate()
        val confession = Confession.write(
            id = confessionId,
            authorId = authorId,
            content = ConfessionContent(command.content),
            createdAt = Instant.now(),
        )

        val saved = confessionRepository.save(confession)

        return WriteConfessionResponse.from(saved)
    }
}
