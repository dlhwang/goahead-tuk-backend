package io.goahead.tuk.confession.application.port

import io.goahead.tuk.confession.application.port.command.ReactConfessionCommand
import io.goahead.tuk.common.security.DeviceKeyHasher
import io.goahead.tuk.confession.domain.AuthorId
import io.goahead.tuk.confession.domain.Confession
import io.goahead.tuk.confession.domain.ConfessionContent
import io.goahead.tuk.confession.domain.ConfessionId
import io.goahead.tuk.confession.domain.ReactionCounts
import io.goahead.tuk.confession.domain.repository.ConfessionRepository
import io.goahead.tuk.confession.domain.service.ConfessionReactionSelections
import io.goahead.tuk.confession.enums.ReactionType
import io.goahead.tuk.confession.exception.ConfessionNotFoundException
import io.goahead.tuk.confession.exception.SelfReactionNotAllowedException
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import java.time.Instant
import kotlin.test.Test

class ReactConfessionUseCaseImplTest {

    @Test
    fun `selects reaction when confession exists`() {
        val selections = FakeConfessionReactionSelections()
        val service = ReactConfessionUseCaseImpl(
            confessionRepository = FakeConfessionRepository(confession()),
            reactionSelections = selections,
            deviceKeyHasher = FakeDeviceKeyHasher(),
        )

        service.select(
            ReactConfessionCommand(
                confessionId = "confession-1",
                deviceId = "reader-1",
                type = ReactionType.PRAY.name,
            )
        )

        assertThat(selections.selectedConfessionId).isEqualTo(ConfessionId("confession-1"))
        assertThat(selections.selectedDeviceKey).isEqualTo("hashed-reader-1")
        assertThat(selections.selectedType).isEqualTo(ReactionType.PRAY)
    }

    @Test
    fun `clears reaction idempotently`() {
        val selections = FakeConfessionReactionSelections()
        val service = ReactConfessionUseCaseImpl(
            confessionRepository = FakeConfessionRepository(confession()),
            reactionSelections = selections,
            deviceKeyHasher = FakeDeviceKeyHasher(),
        )

        service.clear(ReactConfessionCommand("confession-1", "reader-1", ReactionType.COMFORT.name))

        assertThat(selections.clearedType).isEqualTo(ReactionType.COMFORT)
        assertThat(selections.clearedDeviceKey).isEqualTo("hashed-reader-1")
    }

    @Test
    fun `throws when confession does not exist`() {
        val selections = FakeConfessionReactionSelections()
        val service = ReactConfessionUseCaseImpl(
            confessionRepository = FakeConfessionRepository(null),
            reactionSelections = selections,
            deviceKeyHasher = FakeDeviceKeyHasher(),
        )

        assertThatThrownBy {
            service.select(
                ReactConfessionCommand(
                    confessionId = "missing-confession",
                    deviceId = "reader-1",
                    type = ReactionType.TOGETHER.name,
                )
            )
        }.isInstanceOf(ConfessionNotFoundException::class.java)

        assertThat(selections.selectedConfessionId).isNull()
    }

    @Test
    fun `rejects authors reacting to their own confession`() {
        val service = ReactConfessionUseCaseImpl(
            confessionRepository = FakeConfessionRepository(confession()),
            reactionSelections = FakeConfessionReactionSelections(),
            deviceKeyHasher = FakeDeviceKeyHasher(),
        )

        assertThatThrownBy {
            service.select(ReactConfessionCommand("confession-1", "author-1", ReactionType.PRAY.name))
        }.isInstanceOf(SelfReactionNotAllowedException::class.java)
    }

    private class FakeConfessionRepository(
        private val confession: Confession?,
    ) : ConfessionRepository {
        override fun existsById(confessionId: ConfessionId): Boolean = confession?.id == confessionId

        override fun findById(confessionId: ConfessionId): Confession? = confession?.takeIf { it.id == confessionId }

        override fun findAll(): List<Confession> = emptyList()

        override fun save(confession: Confession): Confession = confession
    }

    private class FakeConfessionReactionSelections : ConfessionReactionSelections {
        var selectedConfessionId: ConfessionId? = null
        var selectedDeviceKey: String? = null
        var selectedType: ReactionType? = null
        var clearedDeviceKey: String? = null
        var clearedType: ReactionType? = null

        override fun select(confessionId: ConfessionId, deviceKey: String, type: ReactionType) {
            selectedConfessionId = confessionId
            selectedDeviceKey = deviceKey
            selectedType = type
        }

        override fun clear(confessionId: ConfessionId, deviceKey: String, type: ReactionType) {
            clearedDeviceKey = deviceKey
            clearedType = type
        }
    }

    private class FakeDeviceKeyHasher : DeviceKeyHasher {
        override fun hash(deviceId: String): String = "hashed-$deviceId"
    }

    private fun confession(): Confession = Confession(
        id = ConfessionId("confession-1"),
        authorId = AuthorId("author-1"),
        content = ConfessionContent("content"),
        reactions = ReactionCounts.empty(),
        createdAt = Instant.EPOCH,
    )
}
