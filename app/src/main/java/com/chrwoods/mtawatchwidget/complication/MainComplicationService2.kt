package com.chrwoods.mtawatchwidget.complication

import android.graphics.drawable.Icon
import androidx.wear.watchface.complications.data.ComplicationData
import androidx.wear.watchface.complications.data.ComplicationType
import androidx.wear.watchface.complications.data.LongTextComplicationData
import androidx.wear.watchface.complications.data.MonochromaticImage
import androidx.wear.watchface.complications.data.PlainComplicationText
import androidx.wear.watchface.complications.data.ShortTextComplicationData
import androidx.wear.watchface.complications.data.SmallImage
import androidx.wear.watchface.complications.data.SmallImageType
import androidx.wear.watchface.complications.datasource.ComplicationRequest
import androidx.wear.watchface.complications.datasource.SuspendingComplicationDataSourceService
import com.chrwoods.mtawatchwidget.R
import com.chrwoods.mtawatchwidget.data.MtaRepository

/**
 * Skeleton for complication data source that returns short text.
 */
class MainComplicationService2 : SuspendingComplicationDataSourceService() {
    private var repository: MtaRepository? = null

    override fun onCreate() {
        super.onCreate()
        // TODO: Replace with MTA API key
        repository = MtaRepository("YOUR_MTA_API_KEY")
    }

    override fun getPreviewData(type: ComplicationType): ComplicationData? {
        println(type)
        if (type == ComplicationType.SHORT_TEXT) {
            return createShortComplicationData("3 min", "Next train in 5 min")
        }
        if (type == ComplicationType.LONG_TEXT) {
            return createLongComplicationData("4 min", "Next train in 5 min")
        }
        return null
    }

    override suspend fun onComplicationRequest(request: ComplicationRequest): ComplicationData? {
        // Use the complication ID as the stop ID, defaulting to L01 if not set
//        val stopId = request.complicationInstanceId.toString().takeIf { it.isNotEmpty() } ?: "L01"
//        val nextTrain = repository?.getNextTrain(stopId)?.first() ?: "Error"
//        return createComplicationData(nextTrain, "Next train: $nextTrain")
        val type = request.complicationType
        println(type)
        if (type == ComplicationType.SHORT_TEXT) {
            return createShortComplicationData("6 min", "Next train in 5 min")
        }
        if (type == ComplicationType.LONG_TEXT) {
            return createLongComplicationData("7 min", "Next train in 5 min")
        }
        return null
    }

    private fun createShortComplicationData(text: String, contentDescription: String) =
        ShortTextComplicationData.Builder(
            text = PlainComplicationText.Builder(text).build(),
            contentDescription = PlainComplicationText.Builder(contentDescription).build()
        ).build()

    private fun createLongComplicationData(text: String, contentDescription: String) =
        LongTextComplicationData.Builder(
                text = PlainComplicationText.Builder(
                    text,
                ).build(),
                contentDescription = PlainComplicationText.Builder(
                    contentDescription,
                ).build(),
            )
                .setMonochromaticImage(
                    MonochromaticImage.Builder(
                        image = Icon.createWithResource(this, R.drawable.splash_icon),
                    ).build(),
                )
                .build()
//            .setSmallImage(
//                SmallImage.Builder(
//                    image = Icon.createWithResource(this, R.drawable.ic_launcher_foreground),
//                    type = SmallImageType.ICON,
//                ).build(),
//            ).build()
}