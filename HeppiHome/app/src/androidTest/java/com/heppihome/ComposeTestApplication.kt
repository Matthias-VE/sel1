package com.heppihome

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import org.junit.Rule
import org.junit.Test

class ComposeTestApplication {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()
    // error met theme

    @Test
    fun testBottomBarExists(){
        composeTestRule.onNodeWithContentDescription("Tasks").assertExists()
        composeTestRule.onNodeWithContentDescription("Overview").assertExists()
        composeTestRule.onNodeWithContentDescription("Settings").assertExists()
    }

    // testTaskNav

    // testOverviewNav

    // testSettingsNav
}