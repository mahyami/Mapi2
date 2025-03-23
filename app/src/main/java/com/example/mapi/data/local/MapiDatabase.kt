package com.google.mapi.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.mapi.data.local.AddressComponentConverters
import com.example.mapi.data.local.AreaSummaryConverter
import com.example.mapi.data.local.AttributionConverters
import com.example.mapi.data.local.BusinessStatusConverter
import com.example.mapi.data.local.ContainingPlaceConverters
import com.example.mapi.data.local.Converters
import com.example.mapi.data.local.GenerativeSummaryConverter
import com.example.mapi.data.local.GoogleMapsLinksConverter
import com.example.mapi.data.local.LocalPlace
import com.example.mapi.data.local.LocalizedTextConverters
import com.example.mapi.data.local.PlacesDao
import com.example.mapi.data.local.PriceLevelConverter
import com.example.mapi.data.local.PriceRangeConverter
import com.example.mapi.data.local.ReviewConverters
import com.example.mapi.data.local.SubDestinationConverters

@Database(
    entities = [
        LocalPlace::class,
    ], version = 1
)
@TypeConverters(
    value = [
        Converters::class,
        ReviewConverters::class,
        AddressComponentConverters::class,
        AttributionConverters::class,
        SubDestinationConverters::class,
        ContainingPlaceConverters::class,
        LocalizedTextConverters::class,
        PriceLevelConverter::class,
        BusinessStatusConverter::class,
        PriceRangeConverter::class,
        GoogleMapsLinksConverter::class,
        AreaSummaryConverter::class,
        GenerativeSummaryConverter::class,
    ]
)
abstract class MapiDatabase : RoomDatabase() {
    abstract fun placeDao(): PlacesDao
}
