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

    // Mock data for different train lines (in seconds)
    private val mockData = mapOf(
        "L01" to listOf(120, 300, 480, 720),  // 1st Ave L train
        "L02" to listOf(60, 240, 420, 600),   // 3rd Ave L train
        "L03" to listOf(180, 360, 540, 780),  // Bedford Ave L train
        "A01" to listOf(240, 420, 660, 900),  // Times Square A train
        "A02" to listOf(120, 300, 480, 720),  // Penn Station A train
        "A03" to listOf(60, 180, 360, 540),   // 14th St A train
        "J01" to listOf(180, 360, 540, 720)   // J train
    )

    fun getNextTrain(stopId: String): Flow<String> = flow {
        try {
            // Use mock data if no API key is provided
            if (apiKey == "YOUR_MTA_API_KEY") {
                val times = mockData[stopId] ?: mockData["J01"]!!
                val nextTrain = times.random()
                emit("$nextTrain")
            } else {
                val response = api.getTripUpdates(apiKey, stopId = stopId)
                val nextTrain = response.tripUpdates
                    .flatMap { it.stopTimeUpdates }
                    .filter { it.stopId == stopId }
                    .minByOrNull { it.arrival?.time ?: Long.MAX_VALUE }

                val seconds = if (nextTrain?.arrival != null) {
                    val arrivalTime = nextTrain.arrival.time
                    val currentTime = System.currentTimeMillis() / 1000
                    (arrivalTime - currentTime).toInt()
                } else {
                    null
                }

                emit(if (seconds != null) "$seconds" else "No trains")
            }
        } catch (e: Exception) {
            emit("Error")
        }
    }
} 