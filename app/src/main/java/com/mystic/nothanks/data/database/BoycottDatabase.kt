package com.mystic.nothanks.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mystic.nothanks.data.database.dao.BoycottDao
import com.mystic.nothanks.data.database.entity.BrandEntity
import com.mystic.nothanks.data.database.entity.CompanyEntity

@Database(
    entities = [BrandEntity::class, CompanyEntity::class],
    version = 1,
    exportSchema = true
)
abstract class BoycottDatabase: RoomDatabase() {
    abstract fun databaseDao(): BoycottDao
}