package io.goahead.tuk.author.application.command

data class SaveAuthorCommand(
    val authorId: String,
    val nickname: String,
)
