package com.example.mapi.data.remote

import kotlinx.serialization.Serializable

@Serializable
data class RemotePlace(
    val name: String = "",
    val id: String = "",
    val displayName: LocalizedText = LocalizedText(),
    val types: List<String> = emptyList(),
    val primaryType: String = "",
    val primaryTypeDisplayName: LocalizedText = LocalizedText(),
    val nationalPhoneNumber: String = "",
    val internationalPhoneNumber: String = "",
    val formattedAddress: String = "",
    val shortFormattedAddress: String = "",
    val postalAddress: PostalAddress = PostalAddress(),
    val addressComponents: List<AddressComponent> = emptyList(),
    val location: LatLng = LatLng(),
    val rating: Double = 0.0,
    val googleMapsUri: String = "",
    val reviews: List<Review> = emptyList(),
    val adrFormatAddress: String = "",
    val businessStatus: BusinessStatus = BusinessStatus.OPERATIONAL,
    val priceLevel: PriceLevel = PriceLevel.PRICE_LEVEL_MODERATE,
    val attributions: List<Attribution> = emptyList(),
    val editorialSummary: LocalizedText = LocalizedText(),
    val paymentOptions: PaymentOptions = PaymentOptions(),
    val parkingOptions: ParkingOptions = ParkingOptions(),
    val subDestinations: List<SubDestination> = emptyList(),
    val generativeSummary: GenerativeSummary = GenerativeSummary(),
    val areaSummary: AreaSummary = AreaSummary(),
    val containingPlaces: List<ContainingPlace> = emptyList(),
    val googleMapsLinks: GoogleMapsLinks = GoogleMapsLinks(),
    val priceRange: PriceRange = PriceRange(),
    val userRatingCount: Int = 0,
    val takeout: Boolean = false,
    val delivery: Boolean = false,
    val dineIn: Boolean = false,
    val curbsidePickup: Boolean = false,
    val reservable: Boolean = false,
    val servesBreakfast: Boolean = false,
    val servesLunch: Boolean = false,
    val servesDinner: Boolean = false,
    val servesBeer: Boolean = false,
    val servesWine: Boolean = false,
    val servesBrunch: Boolean = false,
    val servesVegetarianFood: Boolean = false,
    val outdoorSeating: Boolean = false,
    val liveMusic: Boolean = false,
    val menuForChildren: Boolean = false,
    val servesCocktails: Boolean = false,
    val servesDessert: Boolean = false,
    val servesCoffee: Boolean = false,
    val goodForChildren: Boolean = false,
    val allowsDogs: Boolean = false,
    val restroom: Boolean = false,
    val goodForGroups: Boolean = false,
    val goodForWatchingSports: Boolean = false,
    val pureServiceAreaBusiness: Boolean = false
)

@Serializable
data class LocalizedText(
    val text: String = "",
    val languageCode: String = ""
)

@Serializable
data class PostalAddress(
    val regionCode: String = "",
    val languageCode: String = "",
    val postalCode: String = "",
    val sortingCode: String = "",
    val administrativeArea: String = "",
    val locality: String = "",
    val sublocality: String = "",
    val addressLines: List<String> = emptyList(),
    val recipients: List<String> = emptyList(),
    val organization: String = ""
)

@Serializable
data class AddressComponent(
    val longName: String = "",
    val shortName: String = "",
    val types: List<String> = emptyList()
)

@Serializable
data class LatLng(
    val latitude: Double = 0.0,
    val longitude: Double = 0.0
)

@Serializable
enum class BusinessStatus {
    BUSINESS_STATUS_UNSPECIFIED,
    OPERATIONAL,
    CLOSED_TEMPORARILY,
    CLOSED_PERMANENTLY
}

@Serializable
enum class PriceLevel {
    PRICE_LEVEL_UNSPECIFIED,
    PRICE_LEVEL_FREE,
    PRICE_LEVEL_INEXPENSIVE,
    PRICE_LEVEL_MODERATE,
    PRICE_LEVEL_EXPENSIVE,
    PRICE_LEVEL_VERY_EXPENSIVE
}

@Serializable
data class Attribution(
    val provider: String = "",
    val providerUri: String = ""
)

@Serializable
data class PaymentOptions(
    val acceptsCreditCards: Boolean = false,
    val acceptsDebitCards: Boolean = false,
    val acceptsMobilePayments: Boolean = false,
    val acceptsCash: Boolean = false
)

@Serializable
data class ParkingOptions(
    val streetParking: Boolean = false,
    val garageParking: Boolean = false,
    val valetParking: Boolean = false,
    val privateParking: Boolean = false
)

@Serializable
data class SubDestination(
    val name: String = "",
    val id: String = ""
)

@Serializable
data class GenerativeSummary(
    val overview: LocalizedText = LocalizedText(),
    val overviewFlagContentUri: String = "",
    val description: LocalizedText = LocalizedText(),
    val descriptionFlagContentUri: String = "",
    val references: References = References()
)

@Serializable
data class Review(
    val name: String = "",
    val relativePublishTimeDescription: String = "",
    val text: LocalizedText = LocalizedText(),
    val originalText: LocalizedText = LocalizedText(),
    val rating: Double = 0.0,
    val publishTime: String = "",
    val flagContentUri: String = "",
    val googleMapsUri: String = ""
)

@Serializable
data class References(
    val reviews: List<Review> = emptyList(),
    val places: List<String> = emptyList()
)

@Serializable
data class AreaSummary(
    val contentBlocks: List<ContentBlock> = emptyList(),
    val flagContentUri: String = ""
)

@Serializable
data class ContentBlock(
    val topic: String = "",
    val content: LocalizedText = LocalizedText()
)

@Serializable
data class ContainingPlace(
    val name: String = "",
)

@Serializable
data class GoogleMapsLinks(
    val placeUri: String = "",
)

@Serializable
data class PriceRange(
    val startPrice: Money = Money(),
    val endPrice: Money = Money()
)

@Serializable
data class Money(
    val currencyCode: String = "",
    val units: String = "",
    val nanos: Int = 0
)