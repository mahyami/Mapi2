package com.example.mapi.data.local

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    @TypeConverter
    fun fromStringList(value: List<String>): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toStringList(value: String): List<String> {
        val listType = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(value, listType)
    }
}

class LocalizedTextConverters {
    @TypeConverter
    fun fromLocalizedTextList(value: List<LocalizedText>): String {
        val type = object : TypeToken<List<LocalizedText>>() {}.type
        return Gson().toJson(value, type)
    }

    @TypeConverter
    fun toLocalizedTextList(value: String): List<LocalizedText> {
        val type = object : TypeToken<List<LocalizedText>>() {}.type
        return Gson().fromJson(value, type)
    }
}

class AddressComponentConverters {
    @TypeConverter
    fun fromAddressComponentList(value: List<AddressComponent>): String {
        val type = object : TypeToken<List<AddressComponent>>() {}.type
        return Gson().toJson(value, type)
    }

    @TypeConverter
    fun toAddressComponentList(value: String): List<AddressComponent> {
        val type = object : TypeToken<List<AddressComponent>>() {}.type
        return Gson().fromJson(value, type)
    }
}

class ReviewConverters {
    @TypeConverter
    fun fromReviewList(value: List<LocalReview>): String {
        val type = object : TypeToken<List<LocalReview>>() {}.type
        return Gson().toJson(value, type)
    }

    @TypeConverter
    fun toReviewList(value: String): List<LocalReview> {
        val type = object : TypeToken<List<LocalReview>>() {}.type
        return Gson().fromJson(value, type)
    }
}

class AttributionConverters {
    @TypeConverter
    fun fromAttributionList(value: List<Attribution>): String {
        val type = object : TypeToken<List<Attribution>>() {}.type
        return Gson().toJson(value, type)
    }

    @TypeConverter
    fun toAttributionList(value: String): List<Attribution> {
        val type = object : TypeToken<List<Attribution>>() {}.type
        return Gson().fromJson(value, type)
    }
}

class SubDestinationConverters {
    @TypeConverter
    fun fromSubDestinationList(value: List<SubDestination>): String {
        val type = object : TypeToken<List<SubDestination>>() {}.type
        return Gson().toJson(value, type)
    }

    @TypeConverter
    fun toSubDestinationList(value: String): List<SubDestination> {
        val type = object : TypeToken<List<SubDestination>>() {}.type
        return Gson().fromJson(value, type)
    }
}

class ContainingPlaceConverters {
    @TypeConverter
    fun fromContainingPlaceList(value: List<ContainingPlace>): String {
        val type = object : TypeToken<List<ContainingPlace>>() {}.type
        return Gson().toJson(value, type)
    }

    @TypeConverter
    fun toContainingPlaceList(value: String): List<ContainingPlace> {
        val type = object : TypeToken<List<ContainingPlace>>() {}.type
        return Gson().fromJson(value, type)
    }
}

class BusinessStatusConverter {
    @TypeConverter
    fun fromBusinessStatus(status: BusinessStatus): String {
        return status.name
    }

    @TypeConverter
    fun toBusinessStatus(value: String): BusinessStatus {
        return BusinessStatus.valueOf(value)
    }
}

class PriceLevelConverter {
    @TypeConverter
    fun fromPriceLevel(level: PriceLevel): String {
        return level.name
    }

    @TypeConverter
    fun toPriceLevel(value: String): PriceLevel {
        return PriceLevel.valueOf(value)
    }
}

class GenerativeSummaryConverter {
    @TypeConverter
    fun fromGenerativeSummary(summary: GenerativeSummary): String = Gson().toJson(summary)

    @TypeConverter
    fun toGenerativeSummary(json: String): GenerativeSummary = Gson().fromJson(json, GenerativeSummary::class.java)
}

class AreaSummaryConverter {
    @TypeConverter
    fun fromAreaSummary(areaSummary: AreaSummary): String = Gson().toJson(areaSummary)

    @TypeConverter
    fun toAreaSummary(json: String): AreaSummary = Gson().fromJson(json, AreaSummary::class.java)
}

class GoogleMapsLinksConverter {
    @TypeConverter
    fun fromGoogleMapsLinks(links: GoogleMapsLinks): String = Gson().toJson(links)

    @TypeConverter
    fun toGoogleMapsLinks(json: String): GoogleMapsLinks = Gson().fromJson(json, GoogleMapsLinks::class.java)
}

class PriceRangeConverter {
    @TypeConverter
    fun fromPriceRange(priceRange: PriceRange): String = Gson().toJson(priceRange)

    @TypeConverter
    fun toPriceRange(json: String): PriceRange = Gson().fromJson(json, PriceRange::class.java)
}