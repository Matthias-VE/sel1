package com.heppihome.componentTests

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.platform.app.InstrumentationRegistry
import com.heppihome.R
import com.heppihome.ui.components.Topbar
import com.heppihome.ui.components.TopbarWithOptions
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
        val list : List<()->Unit> = mock()
        composeTestRule.setContent {
            HeppiHomeTheme {
                TopbarWithOptions(title = "TestBar", expanded = true, toggle = test,
                    onBackPressed = test, itemStrings = listOf(""), itemOnClicks = list)
            }
        }
        val context: Context = InstrumentationRegistry.getInstrumentation().targetContext
        @StringRes
        val label : String = context.resources.getString(R.string.InviteToGroup)
        composeTestRule.onNodeWithText(label).assertExists()
    }
}