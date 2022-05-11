package com.heppihome

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.platform.app.InstrumentationRegistry
import com.heppihome.data.FirebaseDao
import com.heppihome.data.HomeRepository
import com.heppihome.ui.components.NewGroup
import com.heppihome.ui.theme.HeppiHomeTheme
import com.heppihome.viewmodels.groups.AddGroupViewModel
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.validateMockitoUsage
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.*


class NewGroupTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Mock
    private lateinit var repository: HomeRepository
    @Mock
    private lateinit var vM : AddGroupViewModel

    @Before
    fun init(){
        MockitoAnnotations.openMocks(this)
        repository = Mockito.mock(HomeRepository(Mockito.mock(FirebaseDao::class.java))::class.java)
        vM = AddGroupViewModel(repository)
    }

    @After
    fun validate() {
        validateMockitoUsage()
    }

    @Test
    fun titleExists(){
        val test = mock<() -> Unit>()
        composeTestRule.setContent {
            HeppiHomeTheme {
                NewGroup(vM = vM, onGroupCancel = test)
            }
        }
        composeTestRule.onNodeWithText("New Group").assertIsDisplayed()
    }

    @Test
    fun testFieldContent(){
        val test = mock<() -> Unit>()
        composeTestRule.setContent {
            HeppiHomeTheme {
                NewGroup(vM = vM, onGroupCancel = test)
            }
        }
        val naam = "Groepsnaam"
        val beschrijving = "De tofste groep"

        val context1: Context = InstrumentationRegistry.getInstrumentation().targetContext
        @StringRes val label1 : String = context1.resources.getString(com.heppihome.R.string.GroupName)
        val context2: Context = InstrumentationRegistry.getInstrumentation().targetContext
        @StringRes val label2 : String = context2.resources.getString(com.heppihome.R.string.Description)

        composeTestRule.onNodeWithText(label1).onSiblings().filter(hasSetTextAction()).onFirst()
            .performTextInput(naam)
        composeTestRule.onNodeWithText(label2).onSiblings().filter(hasSetTextAction()).onLast()
            .performTextInput(beschrijving)
        composeTestRule.onNodeWithText(label1).onSiblings().filter(hasSetTextAction()).onFirst()
            .assertTextEquals(naam)
        composeTestRule.onNodeWithText(label2).onSiblings().filter(hasSetTextAction()).onLast()
            .assertTextEquals(beschrijving)
    }
}
