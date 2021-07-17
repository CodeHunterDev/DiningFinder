package com.elbehiry.shared.data.search

import com.elbehiry.model.SearchItem
import com.elbehiry.shared.data.search.remote.SearchDataSource
import com.elbehiry.shared.data.search.repository.SearchRepository
import com.elbehiry.shared.data.search.repository.SearchRestaurantsRepository
import com.elbehiry.test_shared.MainCoroutineRule
import com.elbehiry.test_shared.SEARCH_ITEM
import com.elbehiry.test_shared.VENUES_ITEMS
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
class SearchRepositoryTest {

    @get:Rule
    var coroutineRule = MainCoroutineRule()

    @Mock
    private lateinit var searchDataSource: SearchDataSource

    private lateinit var searchRepository: SearchRepository
    private val faker by lazy {
        Faker()
    }

    @Before
    fun setup() {
        searchRepository = SearchRestaurantsRepository(searchDataSource)
    }

    @Test
    fun `test search should be successful`() {
        coroutineRule.runBlockingTest {
            whenever(
                searchDataSource.search(
                    any(), any(), any(), any()
                )
            ).thenReturn(
                SEARCH_ITEM
            )

            val items = searchRepository.search(
                "${faker.address().latitude()},${faker.address().longitude()}",
                faker.number().digits(3).toString(),
                faker.number().digits(2).toInt(),
                faker.number().digits(2).toInt()
            )
            Assert.assertEquals(items, VENUES_ITEMS)
        }
    }

    @Test
    fun `test search in case of empty response return empty venues`() {
        coroutineRule.runBlockingTest {
            whenever(
                searchDataSource.search(
                    any(), any(), any(), any()
                )
            ).thenReturn(
                SearchItem()
            )
            val items = searchRepository.search(
                "${faker.address().latitude()},${faker.address().longitude()}",
                faker.number().digits(3).toString(),
                faker.number().digits(2).toInt(),
                faker.number().digits(2).toInt()
            )
            Assert.assertTrue(items.isEmpty())
        }
    }
}