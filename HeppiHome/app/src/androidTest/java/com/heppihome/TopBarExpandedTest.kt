package com.heppihome

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.platform.app.InstrumentationRegistry
import com.heppihome.ui.components.Topbar
import com.heppihome.ui.theme.HeppiHomeTheme
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock

class TopBarExpandedTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var test: () -> Unit

    @Test
    fun testDropdown(){
        test = mock<() -> Unit>()
        composeTestRule.setContent {
            HeppiHomeTheme {
                Topbar(title = "TestBar", expanded = true, toggle = test, onBackPressed = test){}
            }
        }
        val context: Context = InstrumentationRegistry.getInstrumentation().targetContext
        @StringRes
        val label : String = context.resources.getString(R.string.InviteToGroup)
        composeTestRule.onNodeWithText(label).assertExists()
    }
}