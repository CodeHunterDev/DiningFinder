/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.elbehiry.diningfinder.map.binding

import android.view.View
import androidx.annotation.DimenRes
import androidx.annotation.RawRes
import androidx.databinding.BindingAdapter
import com.elbehiry.diningfinder.utils.getFloatUsingCompat
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton

@BindingAdapter("isMapToolbarEnabled")
fun isMapToolbarEnabled(mapView: MapView, isMapToolbarEnabled: Boolean?) {
    if (isMapToolbarEnabled != null) {
        mapView.getMapAsync {
            it.uiSettings.isMapToolbarEnabled = isMapToolbarEnabled
        }
    }
}

@BindingAdapter("isIndoorEnabled")
fun isIndoorEnabled(mapView: MapView, isIndoorEnabled: Boolean?) {
    if (isIndoorEnabled != null) {
        mapView.getMapAsync {
            it.isIndoorEnabled = isIndoorEnabled
        }
    }
}

/**
 * Sets the minimum zoom level of the map (how far out the user is allowed to zoom).
 */
@BindingAdapter("mapMaxZoom")
fun mapZoomLevels(mapView: MapView, @DimenRes maxZoomResId: Int) {
    val maxZoom = mapView.resources.getFloatUsingCompat(maxZoomResId)
    mapView.getMapAsync {
        it.setMaxZoomPreference(maxZoom)
    }
}

@BindingAdapter("mapStyle")
fun mapStyle(mapView: MapView, @RawRes resId: Int) {
    if (resId != 0) {
        mapView.getMapAsync { map ->
            map.setMapStyle(MapStyleOptions.loadRawResourceStyle(mapView.context, resId))
        }
    }
}

@BindingAdapter("goneUnless")
fun goneUnless(view: View, visible: Boolean) {
    view.visibility = if (visible) View.VISIBLE else View.GONE
}

@BindingAdapter("visibleUnless")
fun visibleUnless(view: View, visible: Boolean) {
    view.visibility = if (visible) View.GONE else View.VISIBLE
}

@BindingAdapter("fabVisibility")
fun fabVisibility(fab: FloatingActionButton, visible: Boolean) {
    if (visible) fab.show() else fab.hide()
}
