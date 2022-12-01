package com.example.appnote.util.rules

import com.example.appnote.R
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.rules.TestWatcher
import org.junit.runner.Description

@ExperimentalCoroutinesApi
class NavHostControllerRule(
    private val navGraphId: Int,
    private val currentDestination: Int
) : TestWatcher() {

    private lateinit var testNavHostController: TestNavHostController

    override fun starting(description: Description) {
        testNavHostController = TestNavHostController(ApplicationProvider.getApplicationContext())
        testNavHostController.setGraph(navGraphId)
        testNavHostController.setCurrentDestination(currentDestination)
        super.starting(description)
    }

    fun findTestNavHostController(): TestNavHostController {
        return testNavHostController
    }
}