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

package com.elbehiry.shared.di

import com.elbehiry.shared.data.details.remote.DetailsDataSource
import com.elbehiry.shared.data.details.remote.RestaurantDetailsRemoteDataSource
import com.elbehiry.shared.data.details.repository.DetailsRepository
import com.elbehiry.shared.data.details.repository.DetailsRestaurantRepository
import com.elbehiry.shared.data.remote.DiningApi
import com.elbehiry.shared.data.search.remote.SearchDataSource
import com.elbehiry.shared.data.search.remote.SearchRestaurantsDataSource
import com.elbehiry.shared.data.search.repository.SearchRepository
import com.elbehiry.shared.data.search.repository.SearchRestaurantsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class SearchModule {

    @Provides
    fun provideSearchDataSource(api: DiningApi): SearchDataSource =
        SearchRestaurantsDataSource(api)

    @Provides
    fun provideSearchRepository(
        searchDataSource: SearchDataSource
    ): SearchRepository =
        SearchRestaurantsRepository(searchDataSource)

    @Provides
    fun provideDetailsDataSource(api: DiningApi): DetailsDataSource =
        RestaurantDetailsRemoteDataSource(api)

    @Provides
    fun provideDetailsRepository(
        detailsDataSource: DetailsDataSource
    ): DetailsRepository =
        DetailsRestaurantRepository(detailsDataSource)
}
