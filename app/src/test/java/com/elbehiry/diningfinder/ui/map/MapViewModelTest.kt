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

package com.elbehiry.diningfinder.ui.map

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.elbehiry.diningfinder.ui.map.variant.MapVariant
import com.elbehiry.model.LocationModel
import com.elbehiry.shared.domain.location.GetCurrentLocationUseCase
import com.elbehiry.shared.domain.search.CreateFoursquareVersionUseCase
import com.elbehiry.shared.domain.search.SearchRestaurantsUseCase
import com.elbehiry.shared.result.Result.Error
import com.elbehiry.shared.result.Result.Success
import com.elbehiry.test_shared.faker
import com.elbehiry.test_shared.VENUES_ITEM
import com.elbehiry.test_shared.VENUES_ITEMS
import com.elbehiry.test_shared.MainCoroutineRule
import com.elbehiry.test_shared.runBlockingTest
import com.google.android.gms.maps.model.LatLng
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.flowOf
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

const val currentVersionCode = "20210720"

class MapViewModelTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @MockK
    private lateinit var searchRestaurantsUseCase: SearchRestaurantsUseCase

    @MockK
    private lateinit var createFoursquareVersionUseCase: CreateFoursquareVersionUseCase

    @MockK
    private lateinit var savedStateHandle: SavedStateHandle

    @MockK
    private lateinit var getCurrentLocationUseCase: GetCurrentLocationUseCase
    private val locationModel by lazy {
        LocationModel(
            faker.address().latitude().toDouble(),
            faker.address().longitude().toDouble(),
        )
    }

    private lateinit var mapViewModel: MapViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        coEvery { createFoursquareVersionUseCase(any()) } returns currentVersionCode
        every { savedStateHandle.get<LatLng>(cameraUpdatesKey) } returns null

        mapViewModel = MapViewModel(
            searchRestaurantsUseCase,
            createFoursquareVersionUseCase,
            getCurrentLocationUseCase,
            savedStateHandle
        )
    }

    @Test
    fun `get restaurant by current location returns values success`() =
        mainCoroutineRule.runBlockingTest {
            coEvery { getCurrentLocationUseCase(any()) } returns
                    flowOf(Success(locationModel))
            coEvery { searchRestaurantsUseCase(any()) } returns flowOf(Success(VENUES_ITEMS))
            mapViewModel.getRestaurantByCurrentLocation()
            mapViewModel.venuesList.test {
                Assert.assertEquals(expectItem(), VENUES_ITEMS)
            }
        }

    @Test
    fun `errorMessage flow should has message if get current location return error state`() =
        mainCoroutineRule.runBlockingTest {
            val errorMessage = "Error in getting location"
            coEvery { getCurrentLocationUseCase(any()) } returns
                    flowOf(Error(Exception(errorMessage)))
            coEvery { searchRestaurantsUseCase(any()) } returns flowOf(Success(VENUES_ITEMS))
            mapViewModel.getRestaurantByCurrentLocation()
            mapViewModel.errorMessage.test {
                Assert.assertEquals(expectItem(), errorMessage)
            }
        }

    @Test
    fun `errorMessage empty if get data return success states`() =
        mainCoroutineRule.runBlockingTest {
            coEvery { getCurrentLocationUseCase(any()) } returns
                    flowOf(Success(locationModel))
            coEvery { searchRestaurantsUseCase(any()) } returns flowOf(Success(VENUES_ITEMS))
            mapViewModel.getRestaurantByCurrentLocation()
            mapViewModel.errorMessage.test {
                expectNoEvents()
            }
        }

    @Test
    fun `errorMessage flow should has message if get restaurants return error state`() =
        mainCoroutineRule.runBlockingTest {
            val errorMessage = "Error in getting restaurants"
            coEvery { getCurrentLocationUseCase(any()) } returns
                    flowOf(Success(locationModel))

            coEvery { searchRestaurantsUseCase(any()) } returns
                    flowOf(Error(Exception(errorMessage)))
            mapViewModel.getRestaurantByCurrentLocation()
            mapViewModel.errorMessage.test {
                Assert.assertEquals(expectItem(), errorMessage)
            }
        }

    @Test
    fun `get restaurant emits loading state and remove it after data returned`() =
        mainCoroutineRule.runBlockingTest {
            mapViewModel.isLoading.test {
                Assert.assertTrue(expectItem())
            }
            coEvery { getCurrentLocationUseCase(any()) } returns
                    flowOf(Success(locationModel))
            coEvery { searchRestaurantsUseCase(any()) } returns flowOf(Success(VENUES_ITEMS))
            mapViewModel.getRestaurantByCurrentLocation()
            mapViewModel.isLoading.test {
                Assert.assertFalse(expectItem())
            }
        }

    @Test
    fun `get by specific location does not call location use case and return data successfully`() =
        mainCoroutineRule.runBlockingTest {
            val latitude = faker.address().latitude().toDouble()
            val longitude = faker.address().longitude().toDouble()

            val target = LatLng(latitude, longitude)

            coEvery { searchRestaurantsUseCase(any()) } returns flowOf(Success(VENUES_ITEMS))
            mapViewModel.getRestaurantsInPosition(target)

            coVerify { getCurrentLocationUseCase(any()) wasNot Called }
        }

    @Test
    fun `highlight value should select marker item`() = mainCoroutineRule.runBlockingTest {
        mapViewModel.highlightVenuesItem(VENUES_ITEM)

        mapViewModel.selectedMarkerInfo.test {
            Assert.assertEquals(expectItem(), VENUES_ITEM)
        }
    }

    @Test
    fun `set Map Variant value updates map variant flow`() = mainCoroutineRule.runBlockingTest {
        mapViewModel.setMapVariant(MapVariant.DAY)

        mapViewModel.mapVariant.test {
            Assert.assertEquals(expectItem(), MapVariant.DAY)
        }
    }
}
