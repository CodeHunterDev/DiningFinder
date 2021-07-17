package com.elbehiry.shared.usecase.search

import android.accounts.NetworkErrorException
import com.elbehiry.model.VenuesItem
import com.elbehiry.shared.data.search.repository.SearchRepository
import com.elbehiry.shared.domain.search.SearchRestaurantsUseCase
import com.elbehiry.shared.result.Result
import com.elbehiry.shared.result.data
import com.elbehiry.test_shared.MainCoroutineRule
import com.elbehiry.test_shared.VENUES_ITEMS
import com.elbehiry.test_shared.runBlockingTest
import com.github.javafaker.Faker
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.whenever
import org.assertj.core.api.Assertions.assertThat
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class SearchRestaurantsUseCaseTest {
    @get:Rule
    var coroutineRule = MainCoroutineRule()

    @Mock
    private lateinit var searchRepository: SearchRepository
    private lateinit var searchRestaurantsUseCase: SearchRestaurantsUseCase

    private val faker by lazy {
        Faker()
    }

    @Test
    fun `search for restaurant returns data as Result_Success value`() =
        coroutineRule.runBlockingTest {
            whenever(searchRepository.search(any(), any(), any(), any())).thenReturn(
                VENUES_ITEMS
            )

            searchRestaurantsUseCase = SearchRestaurantsUseCase(
                searchRepository, coroutineRule.testDispatcher
            )

            val restaurants = searchRestaurantsUseCase(createDummyParams())
            assertThat(restaurants is Result.Success)
            Assert.assertEquals(restaurants.data, VENUES_ITEMS)
        }

    @Test
    fun `search failed for restaurant returns data as Result_Error value`() =
        coroutineRule.runBlockingTest {
            searchRestaurantsUseCase = SearchRestaurantsUseCase(
                FakeFailedSearchRepository(), coroutineRule.testDispatcher
            )

            val result = searchRestaurantsUseCase(createDummyParams())
            assertThat(result is Result.Error)
            assertThat((result as Result.Error).exception is NetworkErrorException)
        }

    private fun createDummyParams() = SearchRestaurantsUseCase.Params.create(
        "${faker.address().latitude()},${faker.address().longitude()}",
        faker.number().digits(3).toString(),
        faker.number().digits(2).toInt(),
        faker.number().digits(2).toInt()
    )

    private inner class FakeFailedSearchRepository : SearchRepository {
        override suspend fun search(
            latLng: String,
            version: String,
            radius: Int?,
            limit: Int?
        ): List<VenuesItem> {
            throw NetworkErrorException("Network Failure")
        }
    }
}