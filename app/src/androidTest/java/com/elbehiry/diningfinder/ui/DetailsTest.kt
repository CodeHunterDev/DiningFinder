package com.elbehiry.diningfinder.ui

import android.widget.TextView
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
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
 * Espresso tests for the Details screen.
 */
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class DetailsTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    var activityRule = MainActivityTestRule(R.id.navigation_details)

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun map_basicViewsDisplayed() {
        Espresso.onView(
            CoreMatchers.allOf(
                CoreMatchers.instanceOf(TextView::class.java),
                ViewMatchers.withParent(ViewMatchers.withId(R.id.toolbar))
            )
        ).check(ViewAssertions.matches(ViewMatchers.withText(R.string.title_map_details)))
    }
}