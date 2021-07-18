package com.elbehiry.shared.data.location.repository

import com.elbehiry.model.LocationModel
import com.elbehiry.shared.data.location.remote.ILocationRemoteDataSource
import com.elbehiry.shared.result.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LocationRepository @Inject constructor(
    private val remoteDataSource: ILocationRemoteDataSource
) : ILocationRepository {

    override fun getCurrentLocation(): Flow<Result<LocationModel>> {
        return remoteDataSource.getCurrentLocation().map {
            Result.Success(it)
        }
    }
}
