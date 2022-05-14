package com.heppihome

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import org.junit.Rule
import org.junit.Test

class ComposeTestApplication {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun testBottomBar(){
        composeTestRule.onNodeWithContentDescription("Tasks").assertExists()
        composeTestRule.onNodeWithContentDescription("Overview").assertExists()
        composeTestRule.onNodeWithContentDescription("Settings").assertExists()
    }
}