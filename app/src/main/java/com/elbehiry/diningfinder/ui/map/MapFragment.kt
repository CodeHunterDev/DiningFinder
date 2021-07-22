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

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.view.marginBottom
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.elbehiry.diningfinder.R
import com.elbehiry.diningfinder.databinding.FragmentMapBinding
import com.elbehiry.diningfinder.ui.map.variant.MapVariantSelectionDialogFragment
import com.elbehiry.diningfinder.utils.launchAndRepeatWithViewLifecycle
import com.elbehiry.diningfinder.utils.slideOffsetToAlpha
import com.elbehiry.model.VenuesItem
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.maps.android.ktx.awaitMap
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MapFragment : Fragment() {

    private val viewModel: MapViewModel by viewModels()
    private var mapViewBundle: Bundle? = null
    private lateinit var mapView: MapView
    private lateinit var binding: FragmentMapBinding
    private var fabBaseMarginBottom = 0
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>

    companion object {
        private const val MAPVIEW_BUNDLE_KEY = "MapViewBundleKey"
        private const val ALPHA_TRANSITION_END = 0.5f
        private const val ALPHA_TRANSITION_START = 0.1f
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY)
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            binding.permissionGranted = true
            viewModel.getRestaurantByCurrentLocation()
        } else {
            showLocationPermissionMissingDialog()
        }
    }

    private fun showLocationPermissionMissingDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setMessage(R.string.my_location_rationale)
            .setPositiveButton(android.R.string.ok) { _, _ ->
                requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }.setNegativeButton(android.R.string.cancel, null)
            .show()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMapBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = this@MapFragment.viewModel
        }

        mapView = binding.map.apply {
            onCreate(mapViewBundle)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fabBaseMarginBottom = binding.mapModeFab.marginBottom

        handleToolBarMenuAction()
        configureBottomSheet()

        binding.clickable.setOnClickListener {
            if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_COLLAPSED) {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            } else {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            }
        }

        binding.mapModeFab.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
                ) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                MapVariantSelectionDialogFragment().show(
                    childFragmentManager,
                    "MAP_MODE_DIALOG"
                )
            } else {
                showLocationPermissionMissingDialog()
            }
        }

        binding.locationNeededTxt.setOnClickListener {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }

        validateFabBottomMargin(binding.bottomSheet)
        initView()
        initCallBacks()
        checkPermissionAndGetData()
    }

    private fun initView() {
        viewLifecycleOwner.lifecycleScope.launch {
            mapView.awaitMap().apply {
                setOnMapClickListener { viewModel.dismissFeatureDetails() }

                setOnMarkerClickListener { marker ->
                    viewModel.highlightVenuesItem(
                        venuesItem = marker?.tag as VenuesItem,
                        immediateHighLight = true
                    )
                    false
                }

                setOnInfoWindowClickListener { marker ->
                    navigateToDetails((marker?.tag as VenuesItem).id)
                }
            }
        }
    }

    private fun initCallBacks() {
        launchAndRepeatWithViewLifecycle {
            launch {
                viewModel.mapCenterEvent.collect { update ->
                    mapView.getMapAsync {
                        it.animateCamera(update)
                    }
                }
            }

            launch {
                viewModel.bottomSheetStateEvent.collect { event ->
                    BottomSheetBehavior.from(binding.bottomSheet).state = event
                }
            }

            launch {
                viewModel.selectedMarkerInfo.collect {
                    it?.let {
                        updateInfoSheet(it)
                        listenOnCameraChanges()
                    }
                }
            }

            launch {
                viewModel.errorMessage.collect {
                    Snackbar.make(
                        binding.root,
                        R.string.something_went_worng, Snackbar.LENGTH_SHORT
                    ).show()
                }
            }

            launch {
                viewModel.venuesList.collect { items ->
                    mapView.awaitMap().apply {
                        items.forEach { venue ->
                            val latLng = LatLng(venue.location.lat, venue.location.lng)
                            val marker = this.addMarker(
                                MarkerOptions().position(latLng).title(venue.name)
                            )
                            marker.tag = venue
                            viewModel.highlightVenuesItem(
                                venuesItem = venue,
                                shouldZoomCamera = true
                            )
                        }
                    }
                }
            }
        }
    }

    /**
     * add this listener to prevent the view-model
     * to call the endpoint with the same last called location.
     */
    private fun listenOnCameraChanges() {
        viewLifecycleOwner.lifecycleScope.launch {
            mapView.awaitMap().apply {
                setOnCameraIdleListener {
                    viewModel.getRestaurantsInPosition(this.cameraPosition.target)
                }
            }
        }
    }

    private fun checkPermissionAndGetData() {
        val context = context ?: return
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) ==
            PackageManager.PERMISSION_GRANTED
        ) {
            binding.permissionGranted = true
            lifecycleScope.launch {
                val map = mapView.awaitMap()
                map.isMyLocationEnabled = true
            }

            viewModel.getRestaurantByCurrentLocation()
        } else {
            binding.permissionGranted = false
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private fun navigateToDetails(id: String?) {
        findNavController().navigate(MapFragmentDirections.toDetails(itemId = id))
    }

    private fun handleToolBarMenuAction() {
        binding.toolbar.run {
            setOnMenuItemClickListener { item ->
                if (item.itemId == R.id.action_my_location) {
                    onMyLocationClicked()
                    true
                } else {
                    false
                }
            }
        }
    }

    /**
     * this should move the camera to the current user location on the map.
     * note that this will not triggered if the current location not available.
     */
    private fun onMyLocationClicked() {
    }

    private fun configureBottomSheet() {
        bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet)
        val bottomSheetCallback = object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                val rotation = when (newState) {
                    BottomSheetBehavior.STATE_EXPANDED -> 0f
                    BottomSheetBehavior.STATE_COLLAPSED -> 180f
                    BottomSheetBehavior.STATE_HIDDEN -> 180f
                    else -> return
                }

                binding.expandIcon.animate().rotationX(rotation).start()
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                binding.descriptionScrollview.alpha =
                    slideOffsetToAlpha(
                        slideOffset,
                        ALPHA_TRANSITION_START,
                        ALPHA_TRANSITION_END
                    )
                if (slideOffset > 0f) {
                    binding.mapModeFab.hide()
                } else {
                    binding.mapModeFab.show()
                    validateFabBottomMargin(bottomSheet)
                }
            }
        }
        bottomSheetBehavior.addBottomSheetCallback(bottomSheetCallback)

        binding.bottomSheet.post {
            val state = bottomSheetBehavior.state
            val slideOffset = when (state) {
                BottomSheetBehavior.STATE_EXPANDED -> 1f
                BottomSheetBehavior.STATE_COLLAPSED -> 0f
                else -> -1f
            }
            bottomSheetCallback.onStateChanged(binding.bottomSheet, state)
            bottomSheetCallback.onSlide(binding.bottomSheet, slideOffset)
        }
    }

    private fun validateFabBottomMargin(bottomSheet: View) {
        val ty = (bottomSheet.top - fabBaseMarginBottom - binding.mapModeFab.bottom)
            .coerceAtMost(0)
        binding.mapModeFab.translationY = ty.toFloat()
    }

    private fun updateInfoSheet(venuesItem: VenuesItem) {
        binding.markerTitle.text = venuesItem.name
        binding.markerSubtitle.apply {
            text = venuesItem.location.city
            isVisible = !venuesItem.location.city.isNullOrEmpty()
        }

        val description = venuesItem.location.address ?: ""
        val hasDescription = description.isNotEmpty()
        binding.markerDescription.apply {
            text = description
            isVisible = hasDescription
        }
        binding.expandIcon.isVisible = hasDescription
        binding.clickable.isVisible = hasDescription
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY)
            ?: Bundle().apply { putBundle(MAPVIEW_BUNDLE_KEY, this) }
        mapView.onSaveInstanceState(mapViewBundle)
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
        viewModel.onMapDestroyed()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }
}
