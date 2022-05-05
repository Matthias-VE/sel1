package com.heppihome

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.heppihome.data.FirebaseDao
import com.heppihome.data.HomeRepository
import com.heppihome.ui.components.NewGroup
import com.heppihome.ui.theme.HeppiHomeTheme
import com.heppihome.viewmodels.groups.AddGroupViewModel
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.atLeastOnce
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

class NewGroupTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var repository: HomeRepository
    private lateinit var vM : AddGroupViewModel
    private lateinit var test : () -> Unit

    @Before
    fun init(){
        repository = HomeRepository(FirebaseDao())
        vM = AddGroupViewModel(repository)
        test = mock<()->Unit>()
        composeTestRule.setContent {
            HeppiHomeTheme {
                NewGroup(vM = vM, onGroupCancel = test)
            }
        }
    }

    @Test
    fun testOnClickCancel(){
        composeTestRule.onNodeWithContentDescription("Cancel").performClick()
        composeTestRule.waitForIdle()
        verify(test, atLeastOnce())
    }

    @Test
    fun testOnClickAdd(){
        composeTestRule.onNodeWithText("Add").performClick()
        composeTestRule.waitForIdle()
        verify(vM.addGroups(), atLeastOnce())
    }

    @Test
    fun testFieldContent(){
        val naam = "Groepsnaam"
        val beschrijving = "De tofste groep"
        composeTestRule.onNodeWithText("Name of Group").performTextInput(naam)
        composeTestRule.onNodeWithText("Description").performTextInput(beschrijving)
        composeTestRule.onNodeWithText("Name of Group").assertTextEquals(naam)
        composeTestRule.onNodeWithText("Description").assertTextEquals(beschrijving)
    }
}