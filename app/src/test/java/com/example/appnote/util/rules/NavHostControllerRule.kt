package com.example.appnote.util.rules

import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import com.example.appnote.R
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.rules.TestWatcher
import org.junit.runner.Description

@ExperimentalCoroutinesApi
class NavHostControllerRule(
    private val navGraphId: Int = R.navigation.nav_graph,
    private val currentDestination: Int = 0
) : TestWatcher() {

    private lateinit var testNavHostController: TestNavHostController

    override fun starting(description: Description) {
        testNavHostController = TestNavHostController(ApplicationProvider.getApplicationContext())
        testNavHostController.setGraph(navGraphId)
        testNavHostController.setCurrentDestination(currentDestination)
    }

    fun findTestNavHostController(): TestNavHostController {
        return testNavHostController
    }
}