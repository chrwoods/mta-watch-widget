package com.chrwoods.mtawatchwidget.complication

import android.graphics.drawable.Icon
import androidx.wear.watchface.complications.data.ComplicationData
import androidx.wear.watchface.complications.data.ComplicationText
import androidx.wear.watchface.complications.data.ComplicationType
import androidx.wear.watchface.complications.data.CountDownTimeReference
import androidx.wear.watchface.complications.data.LongTextComplicationData
import androidx.wear.watchface.complications.data.MonochromaticImage
import androidx.wear.watchface.complications.data.NoDataComplicationData
import androidx.wear.watchface.complications.data.PlainComplicationText
import androidx.wear.watchface.complications.data.ShortTextComplicationData
import androidx.wear.watchface.complications.data.SmallImage
import androidx.wear.watchface.complications.data.SmallImageType
import androidx.wear.watchface.complications.data.TimeDifferenceComplicationText
import androidx.wear.watchface.complications.data.TimeDifferenceStyle
import androidx.wear.watchface.complications.datasource.ComplicationRequest
import androidx.wear.watchface.complications.datasource.SuspendingComplicationDataSourceService
import com.chrwoods.mtawatchwidget.R
import com.chrwoods.mtawatchwidget.data.MtaRepository
import kotlinx.coroutines.flow.first
import java.time.Instant
import java.time.temporal.ChronoUnit

/**
 * Skeleton for complication data source that returns short text.
 */
class MainComplicationService : SuspendingComplicationDataSourceService() {
    private var repository: MtaRepository? = null

    override fun onCreate() {
        super.onCreate()
        repository = MtaRepository("YOUR_MTA_API_KEY")
    }

    override fun getPreviewData(type: ComplicationType): ComplicationData? {
        val now = Instant.now()
        val threeMinutesLater = now.plus(3, ChronoUnit.MINUTES)

        return when (type) {
            ComplicationType.SHORT_TEXT -> createShortComplicationData(now, threeMinutesLater)
            ComplicationType.LONG_TEXT -> createLongComplicationData(now, threeMinutesLater)
            else -> null
        }
    }

    override suspend fun onComplicationRequest(request: ComplicationRequest): ComplicationData? {
//        try {
//            val stopId = "J01" // Using J01 as the stop ID for J train
//            val nextTrain = repository?.getNextTrain(stopId)?.first() ?: return NoDataComplicationData()
//
//            val seconds = nextTrain.toIntOrNull() ?: return NoDataComplicationData()
            val now = Instant.now()
            val nextTrainTime = now.plus(310, ChronoUnit.SECONDS)

            return when (request.complicationType) {
                ComplicationType.SHORT_TEXT -> createShortComplicationData(now, nextTrainTime)
                ComplicationType.LONG_TEXT -> createLongComplicationData(now, nextTrainTime)
                else -> NoDataComplicationData()
            }
//        } catch (e: Exception) {
//            return NoDataComplicationData()
//        }
    }

    private fun createShortComplicationData(now: Instant, nextTrainTime: Instant) =
        ShortTextComplicationData.Builder(
//            text = PlainComplicationText.Builder(
//                ComplicationText.TimeDifferenceBuilder()
//                    .setReferencePeriodStartMillis(now.toEpochMilli())
//                    .setReferencePeriodEndMillis(nextTrainTime.toEpochMilli())
//                    .setStyle(ComplicationText.TimeDifferenceBuilder.STYLE_SHORT_SINGLE_UNIT)
//                    .build()
//            ).build(),
//            text = PlainComplicationText.Builder("3 min").build(),
            text = TimeDifferenceComplicationText.Builder(TimeDifferenceStyle.SHORT_DUAL_UNIT, CountDownTimeReference(nextTrainTime)).build(),
            contentDescription = PlainComplicationText.Builder("Next J train").build()
        )
        .setTitle(PlainComplicationText.Builder("J").build())
        .setMonochromaticImage(
            MonochromaticImage.Builder(
                image = Icon.createWithResource(this, R.drawable.ic_j_train_color)
            ).build()
        )
        .build()

    private fun createLongComplicationData(now: Instant, nextTrainTime: Instant) =
        LongTextComplicationData.Builder(
//            text = PlainComplicationText.Builder(
//                PlainComplicationText.TimeDifferenceBuilder()
//                    .setReferencePeriodStartMillis(now.toEpochMilli())
//                    .setReferencePeriodEndMillis(nextTrainTime.toEpochMilli())
//                    .setStyle(PlainComplicationText.TimeDifferenceBuilder.STYLE_SHORT_SINGLE_UNIT)
//                    .build()
//            ).build(),
//            text = PlainComplicationText.Builder("4 min").build(),
            text = TimeDifferenceComplicationText.Builder(TimeDifferenceStyle.SHORT_DUAL_UNIT, CountDownTimeReference(nextTrainTime)).build(),
            contentDescription = PlainComplicationText.Builder("Next J train").build()
        )
        .setTitle(PlainComplicationText.Builder("J").build())
        .setMonochromaticImage(
            MonochromaticImage.Builder(
                image = Icon.createWithResource(this, R.drawable.ic_j_train_color)
            ).build()
        )
        .build()
}