package com.chrwoods.mtawatchwidget.complication

import android.content.Context
import androidx.wear.watchface.complications.data.ComplicationData
import androidx.wear.watchface.complications.data.ComplicationType
import androidx.wear.watchface.complications.data.PlainComplicationText
import androidx.wear.watchface.complications.data.ShortTextComplicationData
import androidx.wear.watchface.complications.datasource.ComplicationRequest
import androidx.wear.watchface.complications.datasource.SuspendingComplicationDataSourceService
import com.chrwoods.mtawatchwidget.data.MtaRepository
import kotlinx.coroutines.flow.first

/**
 * Skeleton for complication data source that returns short text.
 */
class MainComplicationService : SuspendingComplicationDataSourceService() {
    private var repository: MtaRepository? = null

    override fun onCreate() {
        super.onCreate()
        // TODO: Replace with your MTA API key
        repository = MtaRepository("YOUR_MTA_API_KEY")
    }

    override fun getPreviewData(type: ComplicationType): ComplicationData? {
        if (type != ComplicationType.SHORT_TEXT) {
            return null
        }
        return createComplicationData("5 min", "Next train in 5 minutes")
    }

    override suspend fun onComplicationRequest(request: ComplicationRequest): ComplicationData {
        // Use the complication ID as the stop ID, defaulting to L01 if not set
        val stopId = request.complicationInstanceId.toString().takeIf { it.isNotEmpty() } ?: "L01"
        val nextTrain = repository?.getNextTrain(stopId)?.first() ?: "Error"
        return createComplicationData(nextTrain, "Next train: $nextTrain")
    }

    private fun createComplicationData(text: String, contentDescription: String) =
        ShortTextComplicationData.Builder(
            text = PlainComplicationText.Builder(text).build(),
            contentDescription = PlainComplicationText.Builder(contentDescription).build()
        ).build()
}