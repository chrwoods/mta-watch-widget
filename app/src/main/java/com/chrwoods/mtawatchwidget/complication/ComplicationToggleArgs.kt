package com.chrwoods.mtawatchwidget.complication

import android.content.ComponentName
import android.os.Parcel
import android.os.Parcelable
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.longPreferencesKey

/**
 * The arguments for toggling a complication.
 */
data class ComplicationToggleArgs(

    /**
     * The component of the complication being toggled.
     */
    val providerComponent: ComponentName,

    /**
     * An app-defined key for different provided complications.
     */
    val complication: String,

    /**
     * The system-defined key for the instance of a provided complication.
     * (it's entirely possible for the same complication to be used multiple times)
     */
    val complicationInstanceId: Int,
) : Parcelable {
    constructor(parcel: Parcel) : this(
        providerComponent = parcel.readParcelable(ComponentName::class.java.classLoader)!!,
        complication = parcel.readString()!!,
        complicationInstanceId = parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(providerComponent, flags)
        parcel.writeString(complication)
        parcel.writeInt(complicationInstanceId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ComplicationToggleArgs> {
        override fun createFromParcel(parcel: Parcel): ComplicationToggleArgs {
            return ComplicationToggleArgs(parcel)
        }

        override fun newArray(size: Int): Array<ComplicationToggleArgs?> {
            return arrayOfNulls(size)
        }
    }
}

/**
 * Returns the key for the preference used to hold the current state of a given complication.
 */
fun ComplicationToggleArgs.getStatePreferenceKey(): Preferences.Key<Long> =
    longPreferencesKey("${complication}_$complicationInstanceId")