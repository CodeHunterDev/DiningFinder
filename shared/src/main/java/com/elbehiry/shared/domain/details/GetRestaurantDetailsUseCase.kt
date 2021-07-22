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

package com.elbehiry.shared.domain.details

import com.elbehiry.model.RestaurantDetails
import com.elbehiry.shared.data.details.repository.DetailsRepository
import com.elbehiry.shared.di.IoDispatcher
import com.elbehiry.shared.domain.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class GetRestaurantDetailsUseCase @Inject constructor(
    private val detailsRepository: DetailsRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : UseCase<GetRestaurantDetailsUseCase.Params, RestaurantDetails>(ioDispatcher) {

    override suspend fun execute(parameters: Params): RestaurantDetails =
        detailsRepository.getDetails(parameters.venueId, parameters.version)

    class Params private constructor(
        val venueId: String,
        val version: String,
    ) {

        companion object {
            @JvmStatic
            fun create(
                venueId: String,
                version: String,
            ): Params {
                return Params(venueId, version)
            }
        }
    }
}
