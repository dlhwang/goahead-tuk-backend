package io.goahead.tuk.confession.application.port.command

import io.goahead.tuk.confession.domain.AuthorId

class WriteConfessionCommand( val authorId: AuthorId, val content: String) {
}