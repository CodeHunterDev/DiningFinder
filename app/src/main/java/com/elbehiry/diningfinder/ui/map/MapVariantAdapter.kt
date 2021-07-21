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

package com.elbehiry.diningfinder.ui.map

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.elbehiry.diningfinder.R
import com.elbehiry.diningfinder.databinding.ItemMapVariantBinding
import com.elbehiry.diningfinder.ui.map.variant.MapVariant
import com.elbehiry.diningfinder.utils.executeAfter

internal class MapVariantAdapter(
    private val items: List<MapVariant>,
    private val callback: (MapVariant) -> Unit
) : RecyclerView.Adapter<MapVariantViewHolder>() {

    var currentSelection: MapVariant? = null
        set(value) {
            if (field == value) {
                return
            }
            val previous = field
            if (previous != null) {
                notifyItemChanged(items.indexOf(previous))
            }
            field = value
            if (value != null) {
                notifyItemChanged(items.indexOf(value))
            }
        }

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MapVariantViewHolder {
        return MapVariantViewHolder(
            ItemMapVariantBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: MapVariantViewHolder, position: Int) {
        val mapVariant = items[position]
        holder.bind(mapVariant, mapVariant == currentSelection, callback)
    }
}

internal class MapVariantViewHolder(
    private val binding: ItemMapVariantBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(mapVariant: MapVariant, isSelected: Boolean, callback: (MapVariant) -> Unit) {
        binding.executeAfter {
            variant = mapVariant
            isChecked = isSelected
        }
        itemView.setOnClickListener {
            callback(mapVariant)
        }
    }
}

@BindingAdapter("variantIcon")
fun variantIcon(view: TextView, @DrawableRes iconResId: Int) {
    val drawable = AppCompatResources.getDrawable(view.context, iconResId)
    if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
        drawable?.setTintList(
            AppCompatResources.getColorStateList(view.context, R.color.map_variant_icon)
        )
    }
    view.setCompoundDrawablesRelativeWithIntrinsicBounds(drawable, null, null, null)
}
