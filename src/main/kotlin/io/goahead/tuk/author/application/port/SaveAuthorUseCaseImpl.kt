package io.goahead.tuk.author.application.port

import io.goahead.tuk.author.application.command.SaveAuthorCommand
import io.goahead.tuk.author.domain.Author
import io.goahead.tuk.author.domain.AuthorId
import io.goahead.tuk.author.domain.repository.AuthorRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class SaveAuthorUseCaseImpl(
    val authorRepository: AuthorRepository
) : SaveAuthorUseCase {
    override fun execute(command: SaveAuthorCommand) {
        val author = Author(AuthorId(command.authorId), command.nickname, LocalDateTime.now())

        val save = authorRepository.save(author)
    }
}