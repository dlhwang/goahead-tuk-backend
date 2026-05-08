package io.goahead.tuk.confession.domain.repository

import io.goahead.tuk.confession.domain.Confession
import io.goahead.tuk.confession.domain.ConfessionId

interface ConfessionRepository {
    fun existsById(confessionId: ConfessionId): Boolean
    fun findById(confessionId: ConfessionId): Confession?
    fun findAll(): List<Confession>
    fun save(confession: Confession): Confession
}
