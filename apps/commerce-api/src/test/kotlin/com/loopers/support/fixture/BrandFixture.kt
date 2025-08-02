package com.loopers.support.fixture

import com.loopers.domain.brand.Brand

fun createBrand(
    name: String = "테스트 브랜드",
    description: String? = "테스트 브랜드 설명",
    logoUrl: String? = "/test/logo.png",
): Brand = Brand(
    name = name,
    description = description,
    logoUrl = logoUrl,
)
