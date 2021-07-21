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

package com.elbehiry.shared.data.details

import com.elbehiry.shared.data.details.remote.DetailsDataSource
import com.elbehiry.shared.data.details.repository.DetailsRepository
import com.elbehiry.shared.data.details.repository.DetailsRestaurantRepository
import com.elbehiry.test_shared.MainCoroutineRule
import com.elbehiry.test_shared.RESTAURANT_DETAILS
import com.elbehiry.test_shared.faker
import com.elbehiry.test_shared.runBlockingTest
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class DetailsRepositoryTest {
    @get:Rule
    var coroutineRule = MainCoroutineRule()
    @Mock
    private lateinit var detailsDataSource: DetailsDataSource
    private lateinit var detailsRepository: DetailsRepository

    @Before
    fun setup() {
        detailsRepository = DetailsRestaurantRepository(detailsDataSource)
    }

    @Test
    fun `test get details should be successful`() {
        coroutineRule.runBlockingTest {
            whenever(detailsDataSource.getDetails(any(), any())).thenReturn(RESTAURANT_DETAILS)
            val item = detailsRepository.getDetails(
                venueId = faker.number().digits(3).toString(),
                version = faker.number().digits(3).toString()
            )

            Assert.assertEquals(item, RESTAURANT_DETAILS)
        }
    }
}
