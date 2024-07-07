package com.mystic.nothanks.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.Gson
import com.mystic.nothanks.core.models.Brand
import com.mystic.nothanks.util.defaultBrandSeparator

/**
 * A brand is something that a consumer may search to know whether or not to purchase a product.
 * @param name Name of the brand
 * @param description Explanation of why the brand is on this list.  Or, if it's not a brand to avoid, just a description of the brand.
 * @param status Whether to support or avoid this brand
 * @param reasons A list of reasons for why consumers should avoid this brand.
 * Can be null if the status does not equals [BoycottStatus.AVOID]
 * @param countries A list of countries (ISO alpha-2 country codes) that the brand operates in.
 *       Useful for filtering for brands that are relevant to a specific region.
 *       "global" means it is available in all countries.
 * @param categories A list of categories this brand belongs in
 * @param website Website for the brand
 * @param logoUrl Logo of the brand - Logo should be at least 200x200 pixels
 * @param alternatives List of brands that would be an alternative option to purchasing from this brand.
 * @param alternativesDescription Plain text description of alternatives.
 *       Especially useful when alternatives are difficult to enumerate.
 * @param stakeholders If useful, a list of stakeholders, such as companies who own this brand.
 */
@Entity(tableName = "brands")
data class BrandEntity(
    @PrimaryKey val id: String,
    val name: String,
    val description: String,
    val status: String,
    val reasons: String?,
    val countries: String?,
    val categories: String?,
    val website: String?,
    @ColumnInfo(name = "logo_url") val logoUrl: String?,
    val alternatives: String?,
    @ColumnInfo(name = "alternatives_text") val alternativesDescription: String?,
    @ColumnInfo(name = "stakeholder") val stakeholders: String?,
)

fun Brand.toEntity() = BrandEntity(
    id = id,
    name = name,
    description = description,
    status = status.name.lowercase(),
    reasons = reasons?.defaultBrandJoin { it.name.lowercase() },
    countries = countries?.defaultBrandJoin(),
    categories = categories?.defaultBrandJoin { it.name.lowercase() },
    website = website,
    logoUrl = logoUrl,
    alternatives = alternatives?.defaultBrandJoin(),
    alternativesDescription = alternativesDescription,
    stakeholders = stakeholders?.defaultBrandJoin { Gson().toJson(it) }
)

private fun <T> List<T>.defaultBrandJoin(transform: ((T) -> CharSequence)? = null) =
    joinToString(separator = defaultBrandSeparator, transform = transform)