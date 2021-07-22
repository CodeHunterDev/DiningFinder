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

package com.elbehiry.shared.usecase.details

import android.accounts.NetworkErrorException
import com.elbehiry.model.RestaurantDetails
import com.elbehiry.shared.data.details.repository.DetailsRepository
import com.elbehiry.shared.domain.details.GetRestaurantDetailsUseCase
import com.elbehiry.shared.result.Result
import com.elbehiry.shared.result.data
import com.elbehiry.test_shared.MainCoroutineRule
import com.elbehiry.test_shared.RESTAURANT_DETAILS
import com.elbehiry.test_shared.faker
import com.elbehiry.test_shared.runBlockingTest
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.whenever
import org.assertj.core.api.Assertions
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class GetRestaurantDetailsUseCaseTest {

    @get:Rule
    var coroutineRule = MainCoroutineRule()

    @Mock
    private lateinit var detailsRepository: DetailsRepository
    private lateinit var getRestaurantDetailsUseCase: GetRestaurantDetailsUseCase

    @Before
    fun setUp() {
        getRestaurantDetailsUseCase = GetRestaurantDetailsUseCase(
            detailsRepository, coroutineRule.testDispatcher
        )
    }

    @Test
    fun `get restaurant details returns data as Result_Success value`() =
        coroutineRule.runBlockingTest {
            whenever(detailsRepository.getDetails(any(), any())).thenReturn(
                RESTAURANT_DETAILS
            )
            val result = getRestaurantDetailsUseCase(createDummyParams())
            Assertions.assertThat(result is Result.Success)
        }

    @Test
    fun `get restaurant details returns data as Result_Success valuea with expected item value`() =
        coroutineRule.runBlockingTest {
            whenever(detailsRepository.getDetails(any(), any())).thenReturn(
                RESTAURANT_DETAILS
            )
            val result = getRestaurantDetailsUseCase(createDummyParams())
            Assert.assertEquals(result.data, RESTAURANT_DETAILS)
        }

    @Test
    fun `get restaurant details fails should returns data as Result_Error value`() =
        coroutineRule.runBlockingTest {
            getRestaurantDetailsUseCase = GetRestaurantDetailsUseCase(
                FakeFailedDetailsRepository(), coroutineRule.testDispatcher
            )

            val result = getRestaurantDetailsUseCase(createDummyParams())
            Assertions.assertThat(result is Result.Error)
        }

    @Test
    fun `get restaurant details fails should returns data as Result_Error message value`() =
        coroutineRule.runBlockingTest {
            getRestaurantDetailsUseCase = GetRestaurantDetailsUseCase(
                FakeFailedDetailsRepository(), coroutineRule.testDispatcher
            )

            val result = getRestaurantDetailsUseCase(createDummyParams())
            Assertions.assertThat((result as Result.Error).exception is NetworkErrorException)
        }

    private fun createDummyParams() = GetRestaurantDetailsUseCase.Params.create(
        venueId = faker.number().digits(3).toString(),
        version = faker.number().digits(3).toString()
    )

    private inner class FakeFailedDetailsRepository : DetailsRepository {

        override suspend fun getDetails(
            venueId: String,
            version: String
        ): RestaurantDetails {
            throw NetworkErrorException("Network Failure")
        }
    }
}
