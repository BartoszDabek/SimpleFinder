package bdabek.com.simplefinder.models

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by Bartek on 2017-12-27.
 */
data class Distance(
        val text: String,
        val value: Int
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readInt())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(text)
        parcel.writeInt(value)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Distance> {
        override fun createFromParcel(parcel: Parcel): Distance {
            return Distance(parcel)
        }

        override fun newArray(size: Int): Array<Distance?> {
            return arrayOfNulls(size)
        }
    }

}

data class Rows(
        val rows: List<Elements>
)

data class Elements(
        val elements: List<Distances>
)

data class Distances(
        val distance: Distance
)