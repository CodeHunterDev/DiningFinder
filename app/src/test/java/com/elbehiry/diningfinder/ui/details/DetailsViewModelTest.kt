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

package com.elbehiry.diningfinder.ui.details

import app.cash.turbine.test
import com.elbehiry.diningfinder.ui.map.currentVersionCode
import com.elbehiry.shared.domain.details.GetRestaurantDetailsUseCase
import com.elbehiry.shared.domain.search.CreateFoursquareVersionUseCase
import com.elbehiry.shared.result.Result
import com.elbehiry.test_shared.MainCoroutineRule
import com.elbehiry.test_shared.runBlockingTest
import com.elbehiry.test_shared.RESTAURANT_DETAILS
import com.elbehiry.test_shared.faker
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class DetailsViewModelTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @MockK
    private lateinit var getRestaurantDetailsUseCase: GetRestaurantDetailsUseCase

    @MockK
    private lateinit var createFoursquareVersionUseCase: CreateFoursquareVersionUseCase

    private lateinit var viewmodel: DetailsViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        coEvery { createFoursquareVersionUseCase(any()) } returns currentVersionCode

        viewmodel = DetailsViewModel(
            getRestaurantDetailsUseCase,
            createFoursquareVersionUseCase
        )
    }

    @Test
    fun `get restaurant details by item id returns values success`() =
        mainCoroutineRule.runBlockingTest {
            coEvery { getRestaurantDetailsUseCase(any()) } returns
                Result.Success(RESTAURANT_DETAILS)

            viewmodel.getRestaurantDetails(faker.number().digits(3).toString())
            viewmodel.restaurantDetails.test {
                Assert.assertEquals(expectItem(), RESTAURANT_DETAILS)
            }
        }

    @Test
    fun `errorMessage flow should has message if get restaurant details returns error state`() =
        mainCoroutineRule.runBlockingTest {
            val errorMessage = "Error in getting location"
            coEvery { getRestaurantDetailsUseCase(any()) } returns
                Result.Error(Exception(errorMessage))
            viewmodel.getRestaurantDetails(faker.number().digits(3).toString())
            viewmodel.errorMessage.test {
                Assert.assertEquals(expectItem(), errorMessage)
            }
        }

    @Test
    fun `get restaurant details emits loading state and remove it after data returned`() =
        mainCoroutineRule.runBlockingTest {
            viewmodel.isLoading.test {
                Assert.assertFalse(expectItem())
            }
            coEvery { getRestaurantDetailsUseCase(any()) } returns
                Result.Success(RESTAURANT_DETAILS)
            viewmodel.getRestaurantDetails(faker.number().digits(3).toString())
            viewmodel.isLoading.test {
                Assert.assertFalse(expectItem())
            }
        }
}
