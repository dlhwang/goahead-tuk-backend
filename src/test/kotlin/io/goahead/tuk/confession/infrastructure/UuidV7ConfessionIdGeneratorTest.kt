package io.goahead.tuk.confession.infrastructure

import io.goahead.tuk.confession.application.ConfessionIdGenerator
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import kotlin.test.Test

@SpringBootTest
class UuidV7ConfessionIdGeneratorTest @Autowired constructor(
    private val generator : ConfessionIdGenerator
) {
    @Test
    @DisplayName("고해 ID를 생성한다")
    fun generateIdTest() {
        val id = generator.generate()

        println(id.toString())

        assertThat(id.toString()).isNotBlank()
    }

    @Test
    @DisplayName("고해 ID는 생성 순서대로 정렬 가능하다")
    fun generateSortableIdTest() {
        val firstId = generator.generate()
        Thread.sleep(1)
        val secondId = generator.generate()


        assertThat(firstId.value).isLessThan(secondId.value)


        if (firstId.value < secondId.value) println(
            "firstId: ${firstId.value}, secondId: ${secondId.value}"
        )
    }
}