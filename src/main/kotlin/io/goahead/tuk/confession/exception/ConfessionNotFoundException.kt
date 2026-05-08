package io.goahead.tuk.confession.exception

import io.goahead.tuk.confession.domain.ConfessionId

class ConfessionNotFoundException(
    confessionId: ConfessionId
) : RuntimeException("고해를 찾을 수 없습니다. confessionId=${confessionId.value}")