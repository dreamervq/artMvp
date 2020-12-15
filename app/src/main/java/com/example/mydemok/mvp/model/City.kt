/*
 * Copyright (C) 2015 other
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.mydemok.mvp.model

import android.os.Parcel
import android.os.Parcelable
import com.amap.api.location.AMapLocation

/**
 * @author magic-lee
 */
class City(): Parcelable {
    var lat = 0.0
    var lng = 0.0
    var cityCode: String? = null
    var desc: String? = null
    var location: AMapLocation? = null

    constructor(parcel: Parcel) : this() {
        lat = parcel.readDouble()
        lng = parcel.readDouble()
        cityCode = parcel.readString()
        desc = parcel.readString()
        location = parcel.readParcelable(AMapLocation::class.java.classLoader)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeDouble(lat)
        parcel.writeDouble(lng)
        parcel.writeString(cityCode)
        parcel.writeString(desc)
        parcel.writeParcelable(location, flags)
    }

    companion object CREATOR : Parcelable.Creator<City> {
        override fun createFromParcel(parcel: Parcel): City {
            return City(parcel)
        }

        override fun newArray(size: Int): Array<City?> {
            return arrayOfNulls(size)
        }
    }


}