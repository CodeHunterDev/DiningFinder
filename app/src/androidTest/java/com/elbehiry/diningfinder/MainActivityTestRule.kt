package com.elbehiry.diningfinder

import android.content.Intent
import androidx.test.rule.ActivityTestRule

/**
 * ActivityTestRule for [MainActivity] that can launch with any initial navigation target.
 */
class MainActivityTestRule(
    private val initialNavId: Int
) : ActivityTestRule<MainActivity>(MainActivity::class.java) {

    override fun getActivityIntent(): Intent {
        return Intent(Intent.ACTION_MAIN).apply {
            putExtra(MainActivity.EXTRA_NAVIGATION_ID, initialNavId)
        }
    }
}
