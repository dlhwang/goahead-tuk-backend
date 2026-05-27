package io.goahead.tuk.confession.application.port

import io.goahead.tuk.confession.application.port.command.WriteConfessionResponse
import io.goahead.tuk.confession.domain.repository.ConfessionRepository
import io.goahead.tuk.confession.domain.service.ReactionAggregateReader
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ListConfessionsUseCaseImpl(
    private val confessionRepository: ConfessionRepository,
    private val reactionAggregateReader: ReactionAggregateReader,
) : ListConfessionsUseCase {

    @Transactional(readOnly = true)
    override fun execute(): List<WriteConfessionResponse> {
        val confessions = confessionRepository.findAll()
        val aggregates = reactionAggregateReader.aggregate(confessions.map { it.id })

        return confessions.map { confession ->
            WriteConfessionResponse.from(
                confession.copy(reactions = aggregates[confession.id] ?: confession.reactions)
            )
        }
    }
}
