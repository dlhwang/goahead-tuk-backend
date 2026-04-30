package io.goahead.tuk.confession.infrastructure

import com.fasterxml.uuid.Generators
import com.fasterxml.uuid.impl.TimeBasedEpochGenerator
import io.goahead.tuk.confession.application.ConfessionIdGenerator
import io.goahead.tuk.confession.domain.ConfessionId
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service

@Component
class UuidV7ConfessionIdGenerator : ConfessionIdGenerator {

    private val generator: TimeBasedEpochGenerator = Generators.timeBasedEpochGenerator()

    override fun generate(): ConfessionId {
        val generatedId: String = generator.generate().toString()

        return ConfessionId(generatedId)
    }
}
