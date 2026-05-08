package io.goahead.tuk.confession.application.port

import io.goahead.tuk.confession.application.port.command.ReactConfessionCommand
import io.goahead.tuk.confession.domain.ConfessionId
import io.goahead.tuk.confession.domain.repository.ConfessionRepository
import io.goahead.tuk.confession.domain.service.ConfessionReactionCounter
import io.goahead.tuk.confession.enums.ReactionType
import io.goahead.tuk.confession.exception.ConfessionNotFoundException
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class ReactConfessionUseCaseImpl(
    private val confessionRepository: ConfessionRepository,
    private val reactionCounter: ConfessionReactionCounter,
): ReactConfessionUseCase {

    @Transactional
    override fun execute(command: ReactConfessionCommand) {
        val confessionId = ConfessionId(command.confessionId)
        val type = ReactionType.valueOf(command.type)

        if (!confessionRepository.existsById(confessionId)) {
            throw ConfessionNotFoundException(confessionId)
        }

        reactionCounter.increase(
            confessionId = confessionId,
            type = type,
        )
    }
}
