package com.example.mapi.data.local

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

@Entity(tableName = "places")
data class LocalPlace(
    @PrimaryKey val placeId: String,
    val name: String,
    @Embedded(prefix = "displayName_")
    val displayName: LocalizedText,
    @TypeConverters(Converters::class)
    val types: List<String>,
    val primaryType: String,
    @Embedded(prefix = "primaryTypeDisplayName_")
    val primaryTypeDisplayName: LocalizedText,
    val nationalPhoneNumber: String,
    val internationalPhoneNumber: String,
    val formattedAddress: String,
    val shortFormattedAddress: String,
    @Embedded(prefix = "postalAddress_")
    val postalAddress: PostalAddress,
    @TypeConverters(AddressComponentConverters::class)
    val addressComponents: List<AddressComponent>,
    @Embedded
    val location: LatLng,
    val rating: Double,
    val googleMapsUri: String,
    @TypeConverters(ReviewConverters::class)
    val reviews: List<LocalReview>,
    val adrFormatAddress: String,
    val businessStatus: BusinessStatus,
    val priceLevel: PriceLevel,
    @TypeConverters(AttributionConverters::class)
    val attributions: List<Attribution>,
    @Embedded(prefix = "editorialSummary_")
    val editorialSummary: LocalizedText,
    @Embedded
    val paymentOptions: PaymentOptions,
    @Embedded
    val parkingOptions: ParkingOptions,
    @TypeConverters(SubDestinationConverters::class)
    val subDestinations: List<SubDestination>,
    @TypeConverters(GenerativeSummaryConverter::class)
    val generativeSummary: GenerativeSummary,
    @TypeConverters(AreaSummaryConverter::class)
    val areaSummary: AreaSummary,
    @TypeConverters(ContainingPlaceConverters::class)
    val containingPlaces: List<ContainingPlace>,
    @TypeConverters(GoogleMapsLinksConverter::class)
    val googleMapsLinks: GoogleMapsLinks,
    @TypeConverters(PriceRangeConverter::class)
    val priceRange: PriceRange,
    val userRatingCount: Int,
    val takeout: Boolean,
    val delivery: Boolean,
    val dineIn: Boolean,
    val curbsidePickup: Boolean,
    val reservable: Boolean,
    val servesBreakfast: Boolean,
    val servesLunch: Boolean,
    val servesDinner: Boolean,
    val servesBeer: Boolean,
    val servesWine: Boolean,
    val servesBrunch: Boolean,
    val servesVegetarianFood: Boolean,
    val outdoorSeating: Boolean,
    val liveMusic: Boolean,
    val menuForChildren: Boolean,
    val servesCocktails: Boolean,
    val servesDessert: Boolean,
    val servesCoffee: Boolean,
    val goodForChildren: Boolean,
    val allowsDogs: Boolean,
    val restroom: Boolean,
    val goodForGroups: Boolean,
    val goodForWatchingSports: Boolean,
    val pureServiceAreaBusiness: Boolean
)
data class LocalizedText(
    val text: String,
    val languageCode: String
)

data class PostalAddress(
    val regionCode: String,
    val languageCode: String,
    val postalCode: String,
    val sortingCode: String,
    val administrativeArea: String,
    val locality: String,
    val sublocality: String,
    val addressLines: List<String>,
    val recipients: List<String>,
    val organization: String
)

data class AddressComponent(
    val longName: String,
    val shortName: String,
    val types: List<String>
)

data class LatLng(
    val latitude: Double,
    val longitude: Double
)

enum class BusinessStatus {
    BUSINESS_STATUS_UNSPECIFIED,
    OPERATIONAL,
    CLOSED_TEMPORARILY,
    CLOSED_PERMANENTLY
}

enum class PriceLevel {
    PRICE_LEVEL_UNSPECIFIED,
    PRICE_LEVEL_FREE,
    PRICE_LEVEL_INEXPENSIVE,
    PRICE_LEVEL_MODERATE,
    PRICE_LEVEL_EXPENSIVE,
    PRICE_LEVEL_VERY_EXPENSIVE
}

data class Attribution(
    val provider: String,
    val providerUri: String
)

data class PaymentOptions(
    val acceptsCreditCards: Boolean,
    val acceptsDebitCards: Boolean,
    val acceptsMobilePayments: Boolean,
    val acceptsCash: Boolean
)

data class ParkingOptions(
    val streetParking: Boolean,
    val garageParking: Boolean,
    val valetParking: Boolean,
    val privateParking: Boolean
)

data class SubDestination(
    val name: String,
    val id: String
)

data class GenerativeSummary(
    val overview: LocalizedText,
    val overviewFlagContentUri: String,
    val description: LocalizedText,
    val descriptionFlagContentUri: String,
    val references: References
)

data class References(
    val reviews: List<LocalReview>,
    val places: List<String>
)

data class AreaSummary(
    val contentBlocks: List<ContentBlock>,
    val flagContentUri: String
)

data class ContentBlock(
    val topic: String,
    val content: LocalizedText
)

data class ContainingPlace(
    val name: String
)

data class GoogleMapsLinks(
    val placeUri: String
)

data class PriceRange(
    val startPrice: Money,
    val endPrice: Money
)

data class Money(
    val currencyCode: String,
    val units: String,
    val nanos: Int
)

@Entity(tableName = "reviews")
data class LocalReview(
    @PrimaryKey val name: String,
    val relativePublishTimeDescription: String,
    val text: String,
    val rating: Double,
    val publishTime: String,
)