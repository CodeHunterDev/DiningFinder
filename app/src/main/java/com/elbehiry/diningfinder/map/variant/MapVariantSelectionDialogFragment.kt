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

package com.elbehiry.diningfinder.map.variant

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.View
import android.view.WindowManager
import android.view.Gravity
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.RecyclerView
import com.elbehiry.diningfinder.R
import com.elbehiry.diningfinder.map.MapVariantAdapter
import com.elbehiry.diningfinder.map.MapViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MapVariantSelectionDialogFragment : AppCompatDialogFragment() {

    private val mapViewModel: MapViewModel by viewModels(
        ownerProducer = { requireParentFragment() }
    )
    private lateinit var adapter: MapVariantAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_map_variant_select, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = MapVariantAdapter(MapVariant.values().toMutableList(), ::selectMapVariant)
        view.findViewById<RecyclerView>(R.id.map_variant_list).adapter = adapter

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                mapViewModel.mapVariant.collect {
                    adapter.currentSelection = it
                }
            }
        }
    }

    @SuppressLint("RtlHardcoded")
    override fun onStart() {
        super.onStart()
        requireDialog().window?.apply {
            clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
            val isRtl = resources.configuration.layoutDirection == View.LAYOUT_DIRECTION_RTL
            attributes.gravity = Gravity.BOTTOM or if (isRtl) Gravity.LEFT else Gravity.RIGHT
            setBackgroundDrawable(null)
        }
        val margin = resources.getDimensionPixelSize(R.dimen.margin_normal)
        view?.updateLayoutParams<ViewGroup.MarginLayoutParams> {
            setMargins(margin, margin, margin, margin)
        }
    }

    private fun selectMapVariant(mapVariant: MapVariant) {
        mapViewModel.setMapVariant(mapVariant)
        dismiss()
    }
}
