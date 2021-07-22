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

import com.elbehiry.shared.data.details.mapper.toRestaurantModel
import com.elbehiry.shared.data.details.remote.DetailsDataSource
import com.elbehiry.shared.data.details.remote.RestaurantDetailsRemoteDataSource
import com.elbehiry.shared.data.remote.DiningApi
import com.elbehiry.test_shared.DETAIL_ITEM
import com.elbehiry.test_shared.MainCoroutineRule
import com.elbehiry.test_shared.runBlockingTest
import com.github.javafaker.Faker
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
class DetailsDataSourceTest {

    @get:Rule
    var coroutineRule = MainCoroutineRule()

    @Mock
    private lateinit var api: DiningApi
    private val faker by lazy {
        Faker()
    }
    private lateinit var detailsDataSource: DetailsDataSource

    @Before
    fun setup() {
        detailsDataSource = RestaurantDetailsRemoteDataSource(api)
    }

    @Test
    fun `test get details should be successful`() {
        coroutineRule.runBlockingTest {
            whenever(api.getDetails(any(), any(), any(), any())).thenReturn(DETAIL_ITEM)
            val item = detailsDataSource.getDetails(
                venueId = faker.number().digits(3).toString(),
                version = faker.number().digits(3).toString()
            )

            Assert.assertEquals(item, DETAIL_ITEM.toRestaurantModel())
        }
    }
}
