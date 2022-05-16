package com.heppihome.componentTests

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.platform.app.InstrumentationRegistry
import com.heppihome.R
import com.heppihome.data.FirebaseDao
import com.heppihome.data.HomeRepository
import com.heppihome.data.models.Group
import com.heppihome.ui.components.EditGroup
import com.heppihome.ui.theme.HeppiHomeTheme
import com.heppihome.viewmodels.groups.EditGroupViewModel
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.atLeastOnce
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

class EditGroupTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Mock
    private lateinit var repository: HomeRepository
    @Mock
    private lateinit var vM : EditGroupViewModel

    private lateinit var test: () -> Unit

    @Before
    fun init(){
        MockitoAnnotations.openMocks(this)
        repository = Mockito.mock(HomeRepository(Mockito.mock(FirebaseDao::class.java))::class.java)
        vM = EditGroupViewModel(repository)
        test = mock<() -> Unit>()
        composeTestRule.setContent {
            HeppiHomeTheme {
                EditGroup(vM = vM, onGroupCancel = test, g = Group())
            }
        }
    }

    @Test
    fun titleExists(){
        val context: Context = InstrumentationRegistry.getInstrumentation().targetContext
        @StringRes val label : String = context.resources.getString(R.string.EditGroup)
        composeTestRule.onNodeWithText(label).assertIsDisplayed()
    }

    @Test
    fun fieldContentDisplayedCorrectly(){
        val default = "default"

        val context1: Context = InstrumentationRegistry.getInstrumentation().targetContext
        @StringRes val label1 : String = context1.resources.getString(R.string.GroupName)
        val context2: Context = InstrumentationRegistry.getInstrumentation().targetContext
        @StringRes val label2 : String = context2.resources.getString(R.string.Description)

        composeTestRule.onNodeWithText(label1).onSiblings().filter(hasSetTextAction()).onFirst()
            .assertTextEquals(default)
        composeTestRule.onNodeWithText(label2).onSiblings().filter(hasSetTextAction()).onLast()
            .assertTextEquals(default)
    }

    @Test
    fun fieldContentEditable(){
        val groep = "groep 12"
        val beschrijving = "sel groep"

        val context1: Context = InstrumentationRegistry.getInstrumentation().targetContext
        @StringRes val label1 : String = context1.resources.getString(R.string.GroupName)
        val context2: Context = InstrumentationRegistry.getInstrumentation().targetContext
        @StringRes val label2 : String = context2.resources.getString(R.string.Description)

        composeTestRule.onNodeWithText(label1).onSiblings().filter(hasSetTextAction()).onFirst()
            .performTextReplacement(groep)
        composeTestRule.onNodeWithText(label2).onSiblings().filter(hasSetTextAction()).onLast()
            .performTextReplacement(beschrijving)

        composeTestRule.onNodeWithText(label1).onSiblings().filter(hasSetTextAction()).onFirst()
            .assertTextEquals(groep)
        composeTestRule.onNodeWithText(label2).onSiblings().filter(hasSetTextAction()).onLast()
            .assertTextEquals(beschrijving)
    }

    @Test
    fun testOnClickCancel(){
        val context: Context = InstrumentationRegistry.getInstrumentation().targetContext
        @StringRes val label : String = context.resources.getString(R.string.Cancel)

        composeTestRule.onNodeWithContentDescription(label).performClick()
        verify(test, atLeastOnce())()
    }
}