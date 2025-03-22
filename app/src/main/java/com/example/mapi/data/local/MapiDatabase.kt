package com.google.mapi.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.mapi.data.local.PlacesDao

@Database(
    entities = [
        LocalPlace::class,
    ], version = 1
)
@TypeConverters(
    value = [
        Converters::class,
        ReviewConverters::class
    ]
)
abstract class MapiDatabase : RoomDatabase() {
    abstract fun placeDao(): PlacesDao
}
