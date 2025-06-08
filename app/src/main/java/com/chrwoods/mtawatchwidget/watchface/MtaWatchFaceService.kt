package com.chrwoods.mtawatchwidget.watchface

import android.view.SurfaceHolder
import androidx.wear.watchface.ComplicationSlotsManager
import androidx.wear.watchface.WatchFace
import androidx.wear.watchface.WatchFaceService
import androidx.wear.watchface.WatchFaceType
import androidx.wear.watchface.style.WatchFaceStyle

class MtaWatchFaceService : WatchFaceService() {
    override fun createEngine(): Engine {
        return Engine()
    }

    inner class Engine : WatchFaceService.Engine() {
        private lateinit var watchFace: WatchFace

        override fun onCreate(holder: SurfaceHolder) {
            super.onCreate(holder)
            setWatchFaceStyle(
                WatchFaceStyle.Builder(this@MtaWatchFaceService)
                    .setAcceptsTapEvents(true)
                    .setWatchFaceType(WatchFaceType.DIGITAL)
                    .build()
            )

            val complicationSlotsManager = ComplicationSlotsManager(
                this@MtaWatchFaceService,
                watchFaceStyle
            )

            watchFace = WatchFace(
                type = WatchFaceType.DIGITAL,
                complicationSlotsManager = complicationSlotsManager
            )
        }

        override fun onSurfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
            super.onSurfaceChanged(holder, format, width, height)
            watchFace.onSurfaceChanged(holder, format, width, height)
        }

        override fun onVisibilityChanged(visible: Boolean) {
            super.onVisibilityChanged(visible)
            watchFace.onVisibilityChanged(visible)
        }

        override fun onTimeTick() {
            super.onTimeTick()
            watchFace.onTimeTick()
        }
    }
} 