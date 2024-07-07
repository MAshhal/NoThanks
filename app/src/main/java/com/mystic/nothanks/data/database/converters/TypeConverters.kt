package com.mystic.nothanks.data.database.converters

import androidx.room.TypeConverter
import com.mystic.nothanks.data.dataset.BoycottReason
import com.mystic.nothanks.data.dataset.BoycottStatus
import java.util.Locale

class TypeConverters {

    @TypeConverter
    fun statusToText(value: BoycottStatus) : String {
        return value.name.lowercase(Locale.US)
    }

    @TypeConverter
    fun fromStatusText(value: String) : BoycottStatus {
        return BoycottStatus.valueOf(value.uppercase(Locale.US))
    }

}