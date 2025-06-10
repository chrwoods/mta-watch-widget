package com.chrwoods.mtawatchwidget.complication

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.os.BundleCompat
import androidx.wear.watchface.complications.datasource.ComplicationDataSourceUpdateRequester
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

/**
 * Receives intents on tap and causes complication states to be toggled and updated.
 */
class ComplicationToggleReceiver : BroadcastReceiver() {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    override fun onReceive(context: Context, intent: Intent) {
        val args = intent.getArgs()

        val result = goAsync()

        scope.launch {
            try {
                args.updateState(context)

                // Request an update for the complication that has just been toggled.
                ComplicationDataSourceUpdateRequester
                    .create(
                        context = context,
                        complicationDataSourceComponent = args.providerComponent,
                    )
                    .requestUpdate(args.complicationInstanceId)
            } finally {
                // Always call finish, even if cancelled
                result.finish()
            }
        }
    }

    companion object {
        private const val EXTRA_ARGS = "arguments"

        /**
         * Returns a pending intent, suitable for use as a tap intent, that causes a complication to be
         * toggled and updated.
         */
        fun getComplicationToggleIntent(
            context: Context,
            args: ComplicationToggleArgs,
        ): PendingIntent {
            val intent = Intent(context, ComplicationToggleReceiver::class.java).apply {
                putExtra(EXTRA_ARGS, args)
            }
            // Pass complicationId as the requestCode to ensure that different complications get
            // different intents.
            return PendingIntent.getBroadcast(
                context,
                args.complicationInstanceId,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
            )
        }

        /**
         * Returns the [ComplicationToggleArgs] from the [Intent] sent to the [ComplicationToggleArgs].
         */
        private fun Intent.getArgs(): ComplicationToggleArgs = requireNotNull(
            BundleCompat.getParcelable(
                this.extras!!,
                EXTRA_ARGS,
                ComplicationToggleArgs::class.java,
            ),
        )
    }
}