package com.elbehiry.shared.di

import android.content.Context
import com.elbehiry.shared.data.location.remote.ILocationRemoteDataSource
import com.elbehiry.shared.data.location.remote.LocationRemoteSource
import com.elbehiry.shared.data.location.repository.ILocationRepository
import com.elbehiry.shared.data.location.repository.LocationRepository
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.SettingsClient
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class LocationModule {

    @Provides
    internal fun providesFusedLocationProviderClient(
        @ApplicationContext context: Context
    ): FusedLocationProviderClient {
        return LocationServices.getFusedLocationProviderClient(context.applicationContext)
    }

    @Provides
    internal fun providesCancellationToken(): CancellationToken {
        return CancellationTokenSource().token
    }

    @Provides
    internal fun providesLocationRequest(): LocationRequest {
        return LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
    }

    @Provides
    internal fun providesLocationSettingsRequest(
        locationRequest: LocationRequest
    ): LocationSettingsRequest {
        return LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
            .setAlwaysShow(true)
            .build()
    }

    @Provides
    internal fun providesLocationSettingsClient(
        @ApplicationContext context: Context
    ): SettingsClient {
        return LocationServices.getSettingsClient(context)
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class LocationSourceModule {

    @Binds
    internal abstract fun bindsLocationSource(
        locationSource: LocationRemoteSource
    ): ILocationRemoteDataSource

    @Binds
    internal abstract fun bindsLocationRepository(
        repository: LocationRepository
    ): ILocationRepository
}
