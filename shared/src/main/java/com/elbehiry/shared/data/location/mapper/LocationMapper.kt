package com.elbehiry.shared.data.location.mapper


import android.location.Location
import com.elbehiry.model.LocationModel

fun Location.toDataModel(): LocationModel {
    return LocationModel(
        latitude,
        longitude,
    )
}
