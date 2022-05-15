package com.heppihome

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.heppihome.data.models.Group
import com.heppihome.data.models.Task
import com.heppihome.ui.components.Tasks
import com.heppihome.ui.theme.HeppiHomeTheme
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.*
import java.text.SimpleDateFormat

class TaskViewTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @OptIn(ExperimentalMaterialApi::class)
    @Test
    fun testOnClickTask(){
        val test1 = mock<(Task) -> Unit>()
        val test2 = mock<() -> Unit>()
        val test3 = mock<() -> Unit>()
        val test4 = mock<() -> Unit>()
        val list1 = listOf(Task("test_today"))
        val list2 = listOf(Task("test_tomorrow"))
        composeTestRule.setContent {
            HeppiHomeTheme {
                Tasks(
                    tasksToday = list1,
                    tasksTomorrow = list2,
                    expandMenu = false,
                    toggleMenu = test2,
                    onChecked = test1,
                    group = Group(),
                    onBackPressed = test2,
                    onInvitePerson = test2,
                    SimpleDateFormat("kk:mm", java.util.Locale.getDefault()),
                    true,
                    test3,
                    test4
                )
            }
        }
        composeTestRule.onNodeWithText("test_tommorow").assertExists("task niet zichtbaar")
        composeTestRule.onNodeWithText("test_today").performClick()
        composeTestRule.waitForIdle()
        verify(test1, atLeastOnce())
    }
}