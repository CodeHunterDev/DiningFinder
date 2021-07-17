package com.elbehiry.shared.network

import com.elbehiry.shared.data.remote.DiningApi
import com.elbehiry.test_shared.MainCoroutineRule
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.*
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class DiningFinderServiceTestUsingMockWebServer {

    @get:Rule
    val mockWebServer = MockWebServer()

    @get:Rule
    var coroutineRule = MainCoroutineRule()

    private val moshi by lazy {
        Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    }

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    private lateinit var api: DiningApi

    @Before
    fun setUp() {
        api = retrofit.create(DiningApi::class.java)
    }

    @Test
    fun `test search should be successful`() = runBlocking {
        mockWebServer.enqueue(
            MockResponse()
                .setBody(fakeResponse)
                .setResponseCode(200)
        )

        val ordersResult = api.search(
            latLng = "",
            version = "",
            radius = 3,
            limit = 5

        )
        mockWebServer.takeRequest()

        Assert.assertEquals(ordersResult.response?.venues?.size, 1)

    }

    @After
    fun tearDown() {
        try {
            mockWebServer.shutdown()
        } catch (exception: Exception) {
            exception.printStackTrace()
        }
    }
}

private const val fakeResponse = "{\n" +
        "    \"response\": {\n" +
        "        \"venues\": [\n" +
        "            {\n" +
        "                \"id\": \"5c2fc6bfe1f0aa002c0d85ef\",\n" +
        "                \"name\": \"TS Sublime\",\n" +
        "                \"location\": {\n" +
        "                    \"address\": \"29 Fareek Ali Amer\",\n" +
        "                    \"crossStreet\": \"Makram Ebeid St\",\n" +
        "                    \"lat\": 30.061594,\n" +
        "                    \"lng\": 31.341291,\n" +
        "                    \"labeledLatLngs\": [\n" +
        "                        {\n" +
        "                            \"label\": \"display\",\n" +
        "                            \"lat\": 30.061594,\n" +
        "                            \"lng\": 31.341291\n" +
        "                        }\n" +
        "                    ],\n" +
        "                    \"distance\": 279,\n" +
        "                    \"cc\": \"EG\",\n" +
        "                    \"city\": \"Madīnat an Naşr\",\n" +
        "                    \"state\": \"القاهرة\",\n" +
        "                    \"country\": \"مصر\",\n" +
        "                    \"formattedAddress\": [\n" +
        "                        \"29 Fareek Ali Amer (Makram Ebeid St)\",\n" +
        "                        \"Madīnat an Naşr\",\n" +
        "                        \"القاهرة\",\n" +
        "                        \"مصر\"\n" +
        "                    ]\n" +
        "                },\n" +
        "                \"categories\": [\n" +
        "                    {\n" +
        "                        \"id\": \"4bf58dd8d48988d16a941735\",\n" +
        "                        \"name\": \"Bakery\",\n" +
        "                        \"pluralName\": \"Bakeries\",\n" +
        "                        \"shortName\": \"Bakery\",\n" +
        "                        \"icon\": {\n" +
        "                            \"prefix\": \"https://ss3.4sqi.net/img/categories_v2/food/bakery_\",\n" +
        "                            \"suffix\": \".png\"\n" +
        "                        },\n" +
        "                        \"primary\": true\n" +
        "                    }\n" +
        "                ],\n" +
        "                \"referralId\": \"v-1626541290\",\n" +
        "                \"hasPerk\": false\n" +
        "            }\n" +
        "        ],\n" +
        "        \"confident\": false\n" +
        "    }\n" +
        "}"