package io.goahead.tuk.confession.domain

class ReactionCount private constructor(
    val value: Long
) {
    fun increase(): ReactionCount {
        return ReactionCount(value + 1)
    }

    companion object {
        val ZERO = ReactionCount(0)

        fun of(value: Long): ReactionCount {
            require(value >= 0) {
                "반응 카운트는 음수일 수 없습니다."
            }

            return ReactionCount(value)
        }
    }
}