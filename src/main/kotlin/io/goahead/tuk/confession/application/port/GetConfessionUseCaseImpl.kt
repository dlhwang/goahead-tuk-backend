package io.goahead.tuk.confession.application.port

import io.goahead.tuk.common.security.DeviceKeyHasher
import io.goahead.tuk.confession.application.port.command.GetConfessionResponse
import io.goahead.tuk.confession.domain.ConfessionId
import io.goahead.tuk.confession.domain.repository.ConfessionRepository
import io.goahead.tuk.confession.domain.service.ReactionAggregateReader
import io.goahead.tuk.confession.domain.service.ReactionSelectionReader
import io.goahead.tuk.confession.exception.ConfessionNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class GetConfessionUseCaseImpl(
    private val confessionRepository: ConfessionRepository,
    private val reactionAggregateReader: ReactionAggregateReader,
    private val reactionSelectionReader: ReactionSelectionReader,
    private val deviceKeyHasher: DeviceKeyHasher,
) : GetConfessionUseCase {

    @Transactional(readOnly = true)
    override fun execute(confessionId: String, deviceId: String): GetConfessionResponse {
        val id = ConfessionId(confessionId)
        val confession = confessionRepository.findById(id)
            ?: throw ConfessionNotFoundException(id)
        val deviceKey = deviceKeyHasher.hash(deviceId)
        val selectedTypes = reactionSelectionReader.selectedTypes(id, deviceKey)

        return GetConfessionResponse.from(
            confession.copy(reactions = reactionAggregateReader.aggregate(id)),
            selectedTypes,
        )
    }
}
