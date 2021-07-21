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

package com.elbehiry.diningfinder

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var currentNavId = NAV_ID_NONE
    private lateinit var navController: NavController
    private lateinit var navHostFragment: NavHostFragment

    companion object {
        /**
         * Key for an int extra defining the initial navigation target.
         * actually this helps in testing because Dagger hilt fragments must be attached
         *  to an @AndroidEntryPoint activity.
         */
        const val EXTRA_NAVIGATION_ID = "extra.NAVIGATION_ID"
        private const val NAV_ID_NONE = -1


        private val TOP_LEVEL_DESTINATIONS = setOf(
            R.id.navigation_map,
            R.id.navigation_details
        )

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        navController = navHostFragment.navController

        if (savedInstanceState == null) {
            currentNavId = navController.graph.startDestination
            val requestedNavId = intent.getIntExtra(EXTRA_NAVIGATION_ID, currentNavId)
            navigateTo(requestedNavId)
        }
    }

    private fun navigateTo(navId: Int) {
        if (navId == currentNavId) {
            return
        }
        navController.navigate(navId)
    }
}
