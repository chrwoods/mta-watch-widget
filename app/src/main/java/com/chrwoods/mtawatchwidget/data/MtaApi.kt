package com.chrwoods.mtawatchwidget.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import retrofit2.http.GET
import retrofit2.http.Query

@Serializable
data class MtaResponse(
    @SerialName("tripUpdates")
    val tripUpdates: List<TripUpdate>
)

@Serializable
data class TripUpdate(
    @SerialName("trip")
    val trip: Trip,
    @SerialName("stopTimeUpdates")
    val stopTimeUpdates: List<StopTimeUpdate>
)

@Serializable
data class Trip(
    @SerialName("routeId")
    val routeId: String,
    @SerialName("tripId")
    val tripId: String
)

@Serializable
data class StopTimeUpdate(
    @SerialName("stopId")
    val stopId: String,
    @SerialName("arrival")
    val arrival: TimeInfo?,
    @SerialName("departure")
    val departure: TimeInfo?
)

@Serializable
data class TimeInfo(
    @SerialName("time")
    val time: Long
)

interface MtaApi {
    @GET("gtfsrt/trips")
    suspend fun getTripUpdates(
        @Query("key") apiKey: String,
        @Query("route_id") routeId: String? = null,
        @Query("stop_id") stopId: String? = null
    ): MtaResponse
} 