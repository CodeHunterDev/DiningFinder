/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.elbehiry.model

import com.squareup.moshi.Json

data class DetailsItem(

    @Json(name = "response")
    val response: DetailsResponse? = null
)

data class DetailsResponse(
    @Json(name = "venue")
    val venue: Venue? = null
)

data class Venue(
    @Json(name = "id")
    val id: String? = null,

    @Json(name = "name")
    val name: String? = null,

    @Json(name = "description")
    val description: String? = null,

    @Json(name = "dislike")
    val dislike: Boolean? = null,

    @Json(name = "createdAt")
    val createdAt: Int? = null,

    @Json(name = "verified")
    val verified: Boolean? = null,

    @Json(name = "contact")
    val contact: Contact? = null,

    @Json(name = "location")
    val location: Location? = null,

    @Json(name = "photos")
    val photos: Photos? = null
)

data class Contact(
    @Json(name = "phone")
    val phone: String? = null,
)

data class Photos(
    @Json(name = "groups")
    val groups: List<GroupsItem?>? = null
)

data class GroupsItem(
    @Json(name = "items")
    val items: List<ItemsItem?>? = null
)

data class ItemsItem(
    @Json(name = "prefix")
    val prefix: String? = null,

    @Json(name = "suffix")
    val suffix: String? = null,

    @Json(name = "width")
    val width: Int? = null,

    @Json(name = "height")
    val height: Int? = null
)
