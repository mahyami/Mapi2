package com.example.mapi.data

import com.example.mapi.data.local.*
import com.example.mapi.data.local.BusinessStatus
import com.example.mapi.data.local.PriceLevel
import com.example.mapi.data.remote.*
import com.example.mapi.data.remote.AddressComponent
import com.example.mapi.data.remote.AreaSummary
import com.example.mapi.data.remote.Attribution
import com.example.mapi.data.remote.ContainingPlace
import com.example.mapi.data.remote.ContentBlock
import com.example.mapi.data.remote.GenerativeSummary
import com.example.mapi.data.remote.GoogleMapsLinks
import com.example.mapi.data.remote.LatLng
import com.example.mapi.data.remote.LocalizedText
import com.example.mapi.data.remote.Money
import com.example.mapi.data.remote.ParkingOptions
import com.example.mapi.data.remote.PaymentOptions
import com.example.mapi.data.remote.PostalAddress
import com.example.mapi.data.remote.PriceRange
import com.example.mapi.data.remote.References
import com.example.mapi.data.remote.SubDestination

fun RemotePlace.toLocalPlace(): LocalPlace {
    return LocalPlace(
        placeId = id,
        name = name,
        displayName = displayName.toLocal(),
        types = types,
        primaryType = primaryType,
        primaryTypeDisplayName = primaryTypeDisplayName.toLocal(),
        nationalPhoneNumber = nationalPhoneNumber,
        internationalPhoneNumber = internationalPhoneNumber,
        formattedAddress = formattedAddress,
        shortFormattedAddress = shortFormattedAddress,
        postalAddress = postalAddress.toLocal(),
        addressComponents = addressComponents.map { it.toLocal() },
        location = location.toLocal(),
        rating = rating,
        googleMapsUri = googleMapsUri,
        reviews = reviews.map { it.toLocal() },
        adrFormatAddress = adrFormatAddress,
        businessStatus = BusinessStatus.valueOf(businessStatus.name),
        priceLevel = PriceLevel.valueOf(priceLevel.name),
        attributions = attributions.map { it.toLocal() },
        editorialSummary = editorialSummary.toLocal(),
        paymentOptions = paymentOptions.toLocal(),
        parkingOptions = parkingOptions.toLocal(),
        subDestinations = subDestinations.map { it.toLocal() },
        generativeSummary = generativeSummary.toLocal(),
        areaSummary = areaSummary.toLocal(),
        containingPlaces = containingPlaces.map { it.toLocal() },
        googleMapsLinks = googleMapsLinks.toLocal(),
        priceRange = priceRange.toLocal(),
        userRatingCount = userRatingCount,
        takeout = takeout,
        delivery = delivery,
        dineIn = dineIn,
        curbsidePickup = curbsidePickup,
        reservable = reservable,
        servesBreakfast = servesBreakfast,
        servesLunch = servesLunch,
        servesDinner = servesDinner,
        servesBeer = servesBeer,
        servesWine = servesWine,
        servesBrunch = servesBrunch,
        servesVegetarianFood = servesVegetarianFood,
        outdoorSeating = outdoorSeating,
        liveMusic = liveMusic,
        menuForChildren = menuForChildren,
        servesCocktails = servesCocktails,
        servesDessert = servesDessert,
        servesCoffee = servesCoffee,
        goodForChildren = goodForChildren,
        allowsDogs = allowsDogs,
        restroom = restroom,
        goodForGroups = goodForGroups,
        goodForWatchingSports = goodForWatchingSports,
        pureServiceAreaBusiness = pureServiceAreaBusiness
    )
}

fun LocalizedText.toLocal(): com.example.mapi.data.local.LocalizedText {
    return com.example.mapi.data.local.LocalizedText(text, languageCode)
}

fun PostalAddress.toLocal(): com.example.mapi.data.local.PostalAddress {
    return com.example.mapi.data.local.PostalAddress(
        regionCode, languageCode, postalCode, sortingCode, administrativeArea,
        locality, sublocality, addressLines, recipients, organization
    )
}

fun AddressComponent.toLocal(): com.example.mapi.data.local.AddressComponent {
    return com.example.mapi.data.local.AddressComponent(longName, shortName, types)
}

fun LatLng.toLocal(): com.example.mapi.data.local.LatLng {
    return com.example.mapi.data.local.LatLng(latitude, longitude)
}

fun Review.toLocal(): LocalReview {
    return LocalReview(
        name, relativePublishTimeDescription, text.text, rating, publishTime
    )
}

fun Attribution.toLocal(): com.example.mapi.data.local.Attribution {
    return com.example.mapi.data.local.Attribution(provider, providerUri)
}

fun PaymentOptions.toLocal(): com.example.mapi.data.local.PaymentOptions {
    return com.example.mapi.data.local.PaymentOptions(
        acceptsCreditCards, acceptsDebitCards, acceptsMobilePayments, acceptsCash
    )
}

fun ParkingOptions.toLocal(): com.example.mapi.data.local.ParkingOptions {
    return com.example.mapi.data.local.ParkingOptions(
        streetParking, garageParking, valetParking, privateParking
    )
}

fun SubDestination.toLocal(): com.example.mapi.data.local.SubDestination {
    return com.example.mapi.data.local.SubDestination(name, id)
}

fun GenerativeSummary.toLocal(): com.example.mapi.data.local.GenerativeSummary {
    return com.example.mapi.data.local.GenerativeSummary(
        overview.toLocal(), overviewFlagContentUri, description.toLocal(),
        descriptionFlagContentUri, references.toLocal()
    )
}

fun References.toLocal(): com.example.mapi.data.local.References {
    return com.example.mapi.data.local.References(
        reviews.map { it.toLocal() }, places
    )
}

fun AreaSummary.toLocal(): com.example.mapi.data.local.AreaSummary {
    return com.example.mapi.data.local.AreaSummary(
        contentBlocks.map { it.toLocal() }, flagContentUri
    )
}

fun ContentBlock.toLocal(): com.example.mapi.data.local.ContentBlock {
    return com.example.mapi.data.local.ContentBlock(topic, content.toLocal())
}

fun ContainingPlace.toLocal(): com.example.mapi.data.local.ContainingPlace {
    return com.example.mapi.data.local.ContainingPlace(name)
}

fun GoogleMapsLinks.toLocal(): com.example.mapi.data.local.GoogleMapsLinks {
    return com.example.mapi.data.local.GoogleMapsLinks(placeUri)
}

fun PriceRange.toLocal(): com.example.mapi.data.local.PriceRange {
    return com.example.mapi.data.local.PriceRange(startPrice.toLocal(), endPrice.toLocal())
}

fun Money.toLocal(): com.example.mapi.data.local.Money {
    return com.example.mapi.data.local.Money(currencyCode, units, nanos)
}