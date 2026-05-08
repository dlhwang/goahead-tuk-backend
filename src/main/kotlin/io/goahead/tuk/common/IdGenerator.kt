package io.goahead.tuk.common

import com.fasterxml.uuid.Generators
import com.fasterxml.uuid.impl.TimeBasedEpochGenerator
import org.springframework.stereotype.Component

@Component
class IdGenerator {
    private val generator: TimeBasedEpochGenerator = Generators.timeBasedEpochGenerator()

    fun generate(): String {
        return generator.generate().toString()
    }

}