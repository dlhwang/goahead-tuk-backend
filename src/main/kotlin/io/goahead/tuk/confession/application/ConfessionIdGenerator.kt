package io.goahead.tuk.confession.application

import io.goahead.tuk.confession.domain.ConfessionId

interface ConfessionIdGenerator {

    fun generate(): ConfessionId
}