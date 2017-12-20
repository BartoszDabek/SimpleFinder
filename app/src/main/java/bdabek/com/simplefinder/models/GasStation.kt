package bdabek.com.simplefinder.models

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by Bartek on 2017-12-19.
 */
data class GasStation(
        val name: String,
        val vicinity: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(vicinity)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<GasStation> {
        override fun createFromParcel(parcel: Parcel): GasStation {
            return GasStation(parcel)
        }

        override fun newArray(size: Int): Array<GasStation?> {
            return arrayOfNulls(size)
        }
    }
}