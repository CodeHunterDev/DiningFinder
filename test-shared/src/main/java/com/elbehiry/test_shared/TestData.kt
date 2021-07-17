package com.elbehiry.test_shared

import com.elbehiry.model.Location
import com.elbehiry.model.Response
import com.elbehiry.model.SearchItem
import com.elbehiry.model.VenuesItem
import com.github.javafaker.Faker

val faker by lazy { Faker() }

val LOCATION = Location(
    country = faker.address().country(),
    lng = faker.address().longitude().toDouble(),
    lat = faker.address().latitude().toDouble(),
    city = faker.address().city()
)

val VENUES_ITEM = VenuesItem(
    id = faker.number().digits(3).toString(),
    referralId = faker.number().digits(2).toString(),
    name = faker.lorem().sentence(),
    location = LOCATION.copy(
        country = faker.address().country()
    )
)

val VENUES_ITEMS = listOf(
    VENUES_ITEM.copy(id = faker.number().digits(3).toString()),
    VENUES_ITEM.copy(id = faker.number().digits(3).toString()),
    VENUES_ITEM.copy(id = faker.number().digits(3).toString()),
    VENUES_ITEM.copy(id = faker.number().digits(3).toString())
)

val RESPONSE = Response(
    venues = VENUES_ITEMS
)

val SEARCH_ITEM = SearchItem(
    response = RESPONSE
)

