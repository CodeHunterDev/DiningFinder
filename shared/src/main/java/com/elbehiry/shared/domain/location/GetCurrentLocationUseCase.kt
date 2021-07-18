package com.elbehiry.shared.domain.location

import com.elbehiry.model.LocationModel
import com.elbehiry.shared.data.location.repository.ILocationRepository
import com.elbehiry.shared.di.IoDispatcher
import com.elbehiry.shared.domain.FlowUseCase
import com.elbehiry.shared.result.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCurrentLocationUseCase @Inject constructor(
    private val locationRepository: ILocationRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
): FlowUseCase<Unit, LocationModel>(ioDispatcher) {

    override fun execute(parameters: Unit): Flow<Result<LocationModel>> {
        return locationRepository.getCurrentLocation()
    }
}