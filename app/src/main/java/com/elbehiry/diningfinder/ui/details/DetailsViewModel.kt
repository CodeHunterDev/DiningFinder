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

package com.elbehiry.diningfinder.ui.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elbehiry.diningfinder.utils.WhileViewSubscribed
import com.elbehiry.diningfinder.utils.tryOffer
import com.elbehiry.model.RestaurantDetails
import com.elbehiry.shared.domain.details.GetRestaurantDetailsUseCase
import com.elbehiry.shared.domain.search.CreateFoursquareVersionUseCase
import com.elbehiry.shared.result.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val getRestaurantDetailsUseCase: GetRestaurantDetailsUseCase,
    private val versionUseCase: CreateFoursquareVersionUseCase
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = Channel<String>(1, BufferOverflow.DROP_LATEST)
    val errorMessage: Flow<String> =
        _errorMessage.receiveAsFlow().shareIn(viewModelScope, WhileViewSubscribed)

    private val _restaurantDetails = MutableStateFlow<RestaurantDetails?>(null)
    val restaurantDetails: StateFlow<RestaurantDetails?> = _restaurantDetails

    fun getRestaurantDetails(venueId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val params = GetRestaurantDetailsUseCase.Params.create(
                venueId,
                versionUseCase(Date())
            )
            val details = getRestaurantDetailsUseCase(params)
            if (details is Result.Success) {
                _restaurantDetails.value = details.data
            } else if (details is Result.Error) {
                _errorMessage.tryOffer(details.exception.message ?: "Error")
            }
            _isLoading.value = false
        }
    }
}
