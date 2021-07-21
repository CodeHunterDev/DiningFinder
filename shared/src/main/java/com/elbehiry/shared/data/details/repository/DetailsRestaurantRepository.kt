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

package com.elbehiry.shared.data.details.repository

import com.elbehiry.model.RestaurantDetails
import com.elbehiry.shared.data.details.remote.DetailsDataSource
import javax.inject.Inject

class DetailsRestaurantRepository @Inject constructor(
    private val detailsDataSource: DetailsDataSource
) : DetailsRepository {

    override suspend fun getDetails(venueId: String, version: String): RestaurantDetails =
        detailsDataSource.getDetails(venueId, version)
}
