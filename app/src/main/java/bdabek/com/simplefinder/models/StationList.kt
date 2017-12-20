package bdabek.com.simplefinder.models

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by Bartek on 2017-12-19.
 */
class StationList(
        val results: List<GasStation>
) : Parcelable {
    constructor(parcel: Parcel) : this(parcel.createTypedArrayList(GasStation))

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeTypedList(results)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<StationList> {
        override fun createFromParcel(parcel: Parcel): StationList {
            return StationList(parcel)
        }

        override fun newArray(size: Int): Array<StationList?> {
            return arrayOfNulls(size)
        }
    }
}