package com.loopers.domain.support

import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable

data class PageCriteria(
    val page: Int = 0,
    val size: Int = 20,
) {
    val pageable: Pageable
        get() = PageRequest.of(page, size)

    fun nextPage(): PageCriteria = copy(page = page + 1)
}
