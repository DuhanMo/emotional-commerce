package com.loopers.domain.user

import com.loopers.domain.BaseEntity
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
) : BaseEntity()
