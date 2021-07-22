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

package com.elbehiry.shared.data.remote

import com.elbehiry.model.DetailsItem
import com.elbehiry.model.SearchItem
import com.elbehiry.shared.BuildConfig
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

const val foodDefaultCategoryId = "4d4b7105d754a06374d81259"

interface DiningApi {

    @GET("v2/venues/search")
    suspend fun search(
        @Query("ll") latLng: String,
        @Query("v") version: String,
        @Query("client_id") clientId: String = BuildConfig.CLIENT_ID,
        @Query("client_secret") clientSecret: String = BuildConfig.SECRET_ID,
        @Query("radius") radius: Int,
        @Query("limit") limit: Int,
        @Query("categoryId") categoryId: String = foodDefaultCategoryId
    ): SearchItem

    @GET("v2/venues/{venue_id}")
    suspend fun getDetails(
        @Path("venue_id") venueId: String,
        @Query("v") version: String,
        @Query("client_id") clientId: String = BuildConfig.CLIENT_ID,
        @Query("client_secret") clientSecret: String = BuildConfig.SECRET_ID
    ): DetailsItem
}
