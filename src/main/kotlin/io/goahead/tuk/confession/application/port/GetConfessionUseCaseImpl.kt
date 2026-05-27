package io.goahead.tuk.confession.application.port

import io.goahead.tuk.confession.application.port.command.WriteConfessionResponse
import io.goahead.tuk.confession.domain.ConfessionId
import io.goahead.tuk.confession.domain.repository.ConfessionRepository
import io.goahead.tuk.confession.domain.service.ReactionAggregateReader
import io.goahead.tuk.confession.exception.ConfessionNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class GetConfessionUseCaseImpl(
    private val confessionRepository: ConfessionRepository,
    private val reactionAggregateReader: ReactionAggregateReader,
) : GetConfessionUseCase {

    @Transactional(readOnly = true)
    override fun execute(confessionId: String): WriteConfessionResponse {
        val id = ConfessionId(confessionId)
        val confession = confessionRepository.findById(id)
            ?: throw ConfessionNotFoundException(id)

        return WriteConfessionResponse.from(
            confession.copy(reactions = reactionAggregateReader.aggregate(id))
        )
    }
}
