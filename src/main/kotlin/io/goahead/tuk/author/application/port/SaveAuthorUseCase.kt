package io.goahead.tuk.author.application.port

import io.goahead.tuk.author.application.command.SaveAuthorCommand

interface SaveAuthorUseCase {

    fun execute(command: SaveAuthorCommand);
}