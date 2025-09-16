package com.commerce.duhan.domain.user

import com.commerce.duhan.domain.common.BaseEntity
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Table

@Table(name = "users")
@Entity
class User(
    val loginId: LoginId,
    val email: Email,
    val birthDate: BirthDate,
    @Enumerated(EnumType.STRING)
    val gender: Gender,
    id: Long = 0L,
) : BaseEntity(id)
