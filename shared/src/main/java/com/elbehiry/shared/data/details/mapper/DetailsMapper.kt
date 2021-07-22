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

package com.elbehiry.shared.data.details.mapper

import com.elbehiry.model.DetailsItem
import com.elbehiry.model.Photo
import com.elbehiry.model.RestaurantDetails

fun DetailsItem.toRestaurantModel(): RestaurantDetails {
    return RestaurantDetails(
        id = response?.venue?.id,
        name = response?.venue?.name,
        description = response?.venue?.description,
        dislike = response?.venue?.dislike,
        createdAt = response?.venue?.createdAt,
        verified = response?.venue?.verified,
        phone = response?.venue?.contact?.phone,
        country = response?.venue?.location?.country,
        address = response?.venue?.location?.address,
        city = response?.venue?.location?.city,
        photoUrl = createPhotoUrl(response?.venue?.bestPhoto)
    )
}

private fun createPhotoUrl(photo: Photo?): String? {
    photo?.let {
        return "${photo.prefix}${photo.width}x${photo.height}${photo.suffix}"
    }
    return null
}
