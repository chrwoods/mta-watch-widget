package com.chrwoods.mtawatchwidget.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Retrofit
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import okhttp3.MediaType.Companion.toMediaType

class MtaRepository(private val apiKey: String) {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api-endpoint.mta.info/")
        .client(
            OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BASIC
                })
                .addInterceptor { chain ->
                    val request = chain.request().newBuilder()
                        .addHeader("x-api-key", apiKey)
                        .build()
                    chain.proceed(request)
                }
                .build()
        )
        .addConverterFactory(Json { ignoreUnknownKeys = true }.asConverterFactory("application/json".toMediaType()))
        .build()

    private val api = retrofit.create(MtaApi::class.java)

    // Mock data for different train lines
    private val mockData = mapOf(
        "L01" to listOf(2, 5, 8, 12),  // 1st Ave L train
        "L02" to listOf(1, 4, 7, 10),  // 3rd Ave L train
        "L03" to listOf(3, 6, 9, 13),  // Bedford Ave L train
        "A01" to listOf(4, 7, 11, 15), // Times Square A train
        "A02" to listOf(2, 5, 8, 12),  // Penn Station A train
        "A03" to listOf(1, 3, 6, 9)    // 14th St A train
    )

    fun getNextTrain(stopId: String): Flow<String> = flow {
        try {
            // Use mock data if no API key is provided
            if (apiKey == "YOUR_MTA_API_KEY") {
                val times = mockData[stopId] ?: mockData["L01"]!!
                val nextTrain = times.random()
                emit("$nextTrain min")
            } else {
                val response = api.getTripUpdates(apiKey, stopId = stopId)
                val nextTrain = response.tripUpdates
                    .flatMap { it.stopTimeUpdates }
                    .filter { it.stopId == stopId }
                    .minByOrNull { it.arrival?.time ?: Long.MAX_VALUE }

                val minutes = if (nextTrain?.arrival != null) {
                    val arrivalTime = nextTrain.arrival.time
                    val currentTime = System.currentTimeMillis() / 1000
                    ((arrivalTime - currentTime) / 60).toInt()
                } else {
                    null
                }

                emit(if (minutes != null) "$minutes min" else "No trains")
            }
        } catch (e: Exception) {
            emit("Error")
        }
    }
} 