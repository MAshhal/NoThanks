package com.mystic.nothanks.core.models

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mystic.nothanks.data.database.entity.BrandEntity
import com.mystic.nothanks.data.dataset.BoycottReason
import com.mystic.nothanks.data.dataset.BoycottStatus
import com.mystic.nothanks.data.dataset.BrandCategory
import com.mystic.nothanks.data.dataset.Stakeholder
import com.mystic.nothanks.util.defaultBrandSeparator
import com.mystic.nothanks.util.engUppercase

/**
 * A brand is something that a consumer may search to know whether or not to purchase a product.
 * @param name Name of the brand
 * @param description Explanation of why the brand is on this list.  Or, if it's not a brand to avoid, just a description of the brand.
 * @param status Whether to support or avoid this brand
 * @param reasons A list of [BoycottReason] for why consumers should avoid this brand.
 * Can be null if the status does not equals [BoycottStatus.AVOID]
 * @param countries A list of countries (ISO alpha-2 country codes) that the brand operates in.
 *       Useful for filtering for brands that are relevant to a specific region.
 *       "global" means it is available in all countries.
 * @param categories A list of [BrandCategory] this brand belongs in
 * @param website Website for the brand
 * @param logoUrl Logo of the brand - Logo should be at least 200x200 pixels
 * @param alternatives List of brands that would be an alternative option to purchasing from this brand.
 * @param alternativesDescription Plain text description of alternatives.
 *       Especially useful when alternatives are difficult to enumerate.
 * @param stakeholders If useful, a list of [Stakeholder], such as companies who own this brand.
 */
data class Brand(
    val id: String,
    val name: String,
    val description: String,
    val status: BoycottStatus,
    val reasons: List<BoycottReason>?,
    val countries: List<String>?, // Could be global or specific countries
    val categories: List<BrandCategory>?,
    val website: String?,
    val logoUrl: String?,
    val alternatives: List<String>?,
    val alternativesDescription: String?,
    val stakeholders: List<Stakeholder>?,
)

fun BrandEntity.toBrand() = Brand(
    id = id,
    name = name,
    description = description,
    status = BoycottStatus.valueOf(status.engUppercase()),
    reasons = reasons?.defaultBrandSplit()?.map { BoycottReason.valueOf(it.engUppercase()) },
    countries = countries?.defaultBrandSplit(),
    categories = categories?.defaultBrandSplit()
        ?.map { BrandCategory.valueOf(it.engUppercase()) },
    website = website,
    logoUrl = logoUrl,
    alternatives = alternatives?.defaultBrandSplit(),
    alternativesDescription = alternativesDescription,
    stakeholders = stakeholders?.let { Gson().fromJson(it, stakeholderTypeToken) }
)

private fun String.defaultBrandSplit() = split(defaultBrandSeparator)
private val stakeholderTypeToken = object : TypeToken<List<Stakeholder>>() {}