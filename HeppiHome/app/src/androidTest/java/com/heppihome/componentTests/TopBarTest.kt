package com.heppihome.componentTests

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.heppihome.ui.components.Topbar
import com.heppihome.ui.theme.HeppiHomeTheme
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import org.mockito.kotlin.atLeastOnce
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

class TopBarTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var test: () -> Unit

    @Before
    fun setUp(){
        test = mock<() -> Unit>()
        composeTestRule.setContent {
            HeppiHomeTheme {
                Topbar(title = "TestBar", expanded = false, toggle = test, onBackPressed = test){}
            }
        }

    }

    @After
    fun validate() {
        Mockito.validateMockitoUsage()
    }

    @Test
    fun titleIsDisplayed(){
        composeTestRule.onNodeWithText("TestBar").assertIsDisplayed()
    }

    @Test
    fun testOnBackPressed(){
        composeTestRule.onNodeWithContentDescription("Return").performClick()
        verify(test, atLeastOnce())()
    }

    @Test
    fun testOnOptionsClick(){
        composeTestRule.onNodeWithContentDescription("Options").performClick()
        verify(test, atLeastOnce())()
    }

}