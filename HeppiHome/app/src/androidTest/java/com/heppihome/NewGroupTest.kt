package com.heppihome

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import com.heppihome.data.FirebaseDao
import com.heppihome.data.HomeRepository
import com.heppihome.ui.components.NewGroup
import com.heppihome.ui.theme.HeppiHomeTheme
import com.heppihome.viewmodels.groups.AddGroupViewModel
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.atLeastOnce
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

class NewGroupTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testOnClickCancel(){
        val test = mock<()->Unit>()
        val rep = HomeRepository(FirebaseDao()) // @Before, bij toevoeging
        composeTestRule.setContent {
            HeppiHomeTheme {
                NewGroup(vM = AddGroupViewModel(rep), onGroupCancel = test)
            }
        }
        composeTestRule.onNodeWithContentDescription("Cancel").performClick()
        composeTestRule.waitForIdle()
        verify(test, atLeastOnce())
    }
}