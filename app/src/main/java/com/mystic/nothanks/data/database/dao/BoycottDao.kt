package com.mystic.nothanks.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.mystic.nothanks.data.database.entity.BrandEntity
import com.mystic.nothanks.data.database.entity.CompanyEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface BoycottDao {

    @Query("SELECT * FROM brands")
    fun getAllBrands() : Flow<List<BrandEntity>>

    @Query("SELECT * FROM companies")
    fun getAllCompanies() : Flow<List<CompanyEntity>>

}