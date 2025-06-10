package com.chrwoods.mtawatchwidget.complication

import android.app.PendingIntent
import android.content.ComponentName
import android.graphics.drawable.Icon
import com.chrwoods.mtawatchwidget.R
import androidx.wear.watchface.complications.data.ComplicationData
import androidx.wear.watchface.complications.data.ComplicationType
import androidx.wear.watchface.complications.data.LongTextComplicationData
import androidx.wear.watchface.complications.data.MonochromaticImage
import androidx.wear.watchface.complications.data.NoDataComplicationData
import androidx.wear.watchface.complications.data.PlainComplicationText
import androidx.wear.watchface.complications.data.SmallImage
import androidx.wear.watchface.complications.data.SmallImageType
import androidx.wear.watchface.complications.datasource.ComplicationRequest
import androidx.wear.watchface.complications.datasource.SuspendingComplicationDataSourceService

class LongTextDataSourceService : SuspendingComplicationDataSourceService() {
    override suspend fun onComplicationRequest(request: ComplicationRequest): ComplicationData? {
        if (request.complicationType != ComplicationType.LONG_TEXT) {
            return NoDataComplicationData()
        }
        val args = ComplicationToggleArgs(
            providerComponent = ComponentName(this, javaClass),
            complication = "LongText",
            complicationInstanceId = request.complicationInstanceId,
        )
        val complicationTogglePendingIntent =
            ComplicationToggleReceiver.getComplicationToggleIntent(
                context = this,
                args = args,
            )
        // Suspending function to retrieve the complication's state
        val state = args.getState(this)
        val case = Case.entries[state.mod(Case.entries.size)]
        return getComplicationData(
            tapAction = complicationTogglePendingIntent,
            case = case,
        )
    }

    override fun getPreviewData(type: ComplicationType): ComplicationData =
        getComplicationData(
            tapAction = null,
            case = Case.TEXT_WITH_ICON_AND_TITLE,
        )

    private fun getComplicationData(
        tapAction: PendingIntent?,
        case: Case,
    ): ComplicationData =
        when (case) {
            Case.TEXT_ONLY -> LongTextComplicationData.Builder(
                text = PlainComplicationText.Builder(
                    text = getText(R.string.long_text_only),
                ).build(),
                contentDescription = PlainComplicationText.Builder(
                    text = getText(R.string.long_text_only_content_description),
                ).build(),
            )
            Case.TEXT_WITH_ICON -> LongTextComplicationData.Builder(
                text = PlainComplicationText.Builder(
                    text = getText(R.string.long_text_with_icon),
                ).build(),
                contentDescription = PlainComplicationText.Builder(
                    text = getText(R.string.long_text_with_icon_content_description),
                ).build(),
            )
                .setMonochromaticImage(
                    MonochromaticImage.Builder(
                        image = Icon.createWithResource(this, R.drawable.splash_icon),
                    ).build(),
                )
            // Unlike for short text complications, if the long title field is supplied then it
            // should always be displayed by the watch face. This means that when a long text
            // provider supplies both title and icon, it is expected that both are displayed.
            Case.TEXT_WITH_ICON_AND_TITLE -> LongTextComplicationData.Builder(
                text = PlainComplicationText.Builder(
                    text = getText(R.string.long_text_with_icon_and_title),
                ).build(),
                contentDescription = PlainComplicationText.Builder(
                    text = getText(R.string.long_text_with_icon_and_title_content_description),
                ).build(),
            )
                .setTitle(
                    PlainComplicationText.Builder(
                        text = getText(R.string.long_title),
                    ).build(),
                )
                .setMonochromaticImage(
                    MonochromaticImage.Builder(
                        image = Icon.createWithResource(this, R.drawable.splash_icon),
                    )
                        .setAmbientImage(
                            ambientImage = Icon.createWithResource(
                                this,
                                R.drawable.splash_icon,
                            ),
                        )
                        .build(),
                )
            Case.TEXT_WITH_TITLE -> LongTextComplicationData.Builder(
                text = PlainComplicationText.Builder(
                    text = getText(R.string.long_text_with_title),
                ).build(),
                contentDescription = PlainComplicationText.Builder(
                    text = getText(R.string.long_text_with_title_content_description),
                ).build(),
            )
                .setTitle(
                    PlainComplicationText.Builder(
                        text = getText(R.string.long_title),
                    ).build(),
                )
            Case.TEXT_WITH_IMAGE -> LongTextComplicationData.Builder(
                text = PlainComplicationText.Builder(
                    text = getText(R.string.long_text_with_image),
                ).build(),
                contentDescription = PlainComplicationText.Builder(
                    text = getText(R.string.long_text_with_image_content_description),
                ).build(),
            )
                .setSmallImage(
                    SmallImage.Builder(
                        image = Icon.createWithResource(this, R.drawable.splash_icon),
                        type = SmallImageType.PHOTO,
                    ).build(),
                )
            Case.TEXT_WITH_IMAGE_AND_TITLE -> LongTextComplicationData.Builder(
                text = PlainComplicationText.Builder(
                    text = getText(R.string.long_text_with_image_and_title),
                ).build(),
                contentDescription = PlainComplicationText.Builder(
                    text = getText(R.string.long_text_with_image_and_title_content_description),
                ).build(),
            )
                .setTitle(
                    PlainComplicationText.Builder(
                        text = getText(R.string.long_title),
                    ).build(),
                )
                .setSmallImage(
                    SmallImage.Builder(
                        image = Icon.createWithResource(this, R.drawable.splash_icon),
                        type = SmallImageType.PHOTO,
                    ).build(),
                )
        }
            .setTapAction(tapAction)
            .build()

    private enum class Case {
        TEXT_ONLY,
        TEXT_WITH_ICON,
        TEXT_WITH_ICON_AND_TITLE,
        TEXT_WITH_TITLE,
        TEXT_WITH_IMAGE,
        TEXT_WITH_IMAGE_AND_TITLE,
    }
}