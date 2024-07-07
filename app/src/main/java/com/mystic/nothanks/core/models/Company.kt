package com.mystic.nothanks.core.models

import com.mystic.nothanks.data.database.entity.CompanyEntity
import com.mystic.nothanks.data.dataset.BoycottStatus
import com.mystic.nothanks.util.engUppercase

data class Company(
    val id: String,
    val name: String,
    val description: String,
    val status: BoycottStatus
)

fun CompanyEntity.toCompany() = Company(
    id = id,
    name = name,
    description = description,
    status = BoycottStatus.valueOf(status.engUppercase())
)

enum class Difficulty {
    EASY, MEDIUM, HARD;
}