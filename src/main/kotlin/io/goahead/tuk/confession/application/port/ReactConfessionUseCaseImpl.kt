package io.goahead.tuk.confession.application.port

import io.goahead.tuk.confession.application.port.command.ReactConfessionCommand
import io.goahead.tuk.common.security.DeviceKeyHasher
import io.goahead.tuk.confession.domain.ConfessionId
import io.goahead.tuk.confession.domain.repository.ConfessionRepository
import io.goahead.tuk.confession.domain.service.ConfessionReactionSelections
import io.goahead.tuk.confession.enums.ReactionType
import io.goahead.tuk.confession.exception.ConfessionNotFoundException
import io.goahead.tuk.confession.exception.SelfReactionNotAllowedException
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class ReactConfessionUseCaseImpl(
    private val confessionRepository: ConfessionRepository,
    private val reactionSelections: ConfessionReactionSelections,
    private val deviceKeyHasher: DeviceKeyHasher,
): ReactConfessionUseCase {

    @Transactional
    override fun select(command: ReactConfessionCommand) {
        val selection = validatedSelection(command)
        reactionSelections.select(selection.confessionId, selection.deviceKey, selection.type)
    }

    @Transactional
    override fun clear(command: ReactConfessionCommand) {
        val selection = validatedSelection(command)
        reactionSelections.clear(selection.confessionId, selection.deviceKey, selection.type)
    }

    private fun validatedSelection(command: ReactConfessionCommand): Selection {
        require(CONFESSION_ID_PATTERN.matches(command.confessionId)) { "Invalid confession identifier" }
        val confessionId = ConfessionId(command.confessionId)
        val type = runCatching { ReactionType.valueOf(command.type.uppercase()) }
            .getOrElse { throw IllegalArgumentException("Invalid reaction type") }
        val confession = confessionRepository.findById(confessionId)
            ?: throw ConfessionNotFoundException(confessionId)

        if (confession.authorId.value == command.deviceId) {
            throw SelfReactionNotAllowedException()
        }

        return Selection(confessionId, deviceKeyHasher.hash(command.deviceId), type)
    }

    private data class Selection(
        val confessionId: ConfessionId,
        val deviceKey: String,
        val type: ReactionType,
    )

    companion object {
        private val CONFESSION_ID_PATTERN = Regex("[A-Za-z0-9_-]{1,64}")
    }
}
