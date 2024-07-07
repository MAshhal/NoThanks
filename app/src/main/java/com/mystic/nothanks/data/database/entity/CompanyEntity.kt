package com.mystic.nothanks.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mystic.nothanks.core.models.Company
import com.mystic.nothanks.util.engLowercase

/**
 * A company is an entity that owns more than one brand, or owns other companies.
 * @param name Name of the company
 * @param description Description of the company, including why it is on the list
 * @param status Whether to support or avoid this company
 *
 */
@Entity(tableName = "companies")
data class CompanyEntity(
    @PrimaryKey val id: String,
    val name: String,
    val description: String,
    val status: String
)

fun Company.toEntity() = CompanyEntity(
    id = id,
    name = name,
    description = description,
    status = status.name.engLowercase()
)