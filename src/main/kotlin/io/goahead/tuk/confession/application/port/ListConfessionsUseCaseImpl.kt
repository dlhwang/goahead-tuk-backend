package io.goahead.tuk.confession.application.port

import io.goahead.tuk.confession.application.port.command.WriteConfessionResponse
import io.goahead.tuk.confession.domain.repository.ConfessionRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ListConfessionsUseCaseImpl(
    private val confessionRepository: ConfessionRepository,
) : ListConfessionsUseCase {

    @Transactional(readOnly = true)
    override fun execute(): List<WriteConfessionResponse> {
        return confessionRepository.findAll().map(WriteConfessionResponse::from)
    }
}
