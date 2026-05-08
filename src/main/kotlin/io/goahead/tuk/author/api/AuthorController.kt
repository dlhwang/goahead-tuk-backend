package io.goahead.tuk.author.api

import io.goahead.tuk.author.api.dto.AuthorResponse
import io.goahead.tuk.author.api.dto.SaveAuthorRequest
import io.goahead.tuk.author.application.AuthorService
import io.goahead.tuk.author.application.command.SaveAuthorCommand
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/authors")
class AuthorController(
    private val authorService: AuthorService
) {

    @GetMapping("/{authorId}")
    fun getAuthor(
        @PathVariable authorId: String
    ): AuthorResponse {
        val author = authorService.getAuthor(authorId)
        return AuthorResponse.from(author)
    }

    @PostMapping
    fun saveAuthor(
        @RequestBody request: SaveAuthorRequest
    ): AuthorResponse {
        val command = SaveAuthorCommand(
            authorId = request.authorId,
            nickname = request.nickname,
        )

        val author = authorService.save(command)
        return AuthorResponse.from(author)
    }
}
