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

package com.elbehiry.diningfinder.ui

import android.widget.TextView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.GrantPermissionRule
import com.elbehiry.diningfinder.MainActivityTestRule
import com.elbehiry.diningfinder.R
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.hamcrest.CoreMatchers
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


/**
 * Espresso tests for the Map screen.
 */
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class MapTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    var activityRule = MainActivityTestRule(R.id.navigation_map)

    @get:Rule (order = 2)
    var permissionRule = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION)

    @Before
    fun setup() {
        hiltRule.inject()
    }

    /**
         * this is an issue with Dagger hilt fragments must be attached
         *  to an @AndroidEntryPoint activity.
         */
//        val navController = TestNavHostController(ApplicationProvider.getApplicationContext())
//        val mapScenario = launchFragmentInContainer<MapFragment>()
//        mapScenario.onFragment { fragment ->
//            navController.setGraph(R.navigation.nav_graph)
//            Navigation.setViewNavController(fragment.requireView(), navController)
//        }

    @Test
    fun map_basicViewsDisplayed() {
        onView(
            CoreMatchers.allOf(
                CoreMatchers.instanceOf(TextView::class.java),
                ViewMatchers.withParent(withId(R.id.toolbar))
            )
        ).check(ViewAssertions.matches(withText(R.string.title_map)))
    }
}
