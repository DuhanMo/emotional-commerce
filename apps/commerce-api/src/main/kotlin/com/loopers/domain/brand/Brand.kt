package com.loopers.domain.brand

import com.loopers.domain.BaseEntity
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Table(name = "brand")
@Entity
class Brand(
    val name: String,
    val description: String? = null,
    val logoUrl: String? = null,
) : BaseEntity()
