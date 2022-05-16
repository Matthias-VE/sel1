package com.heppihome.routeTests

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.platform.app.InstrumentationRegistry
import com.heppihome.R
import com.heppihome.data.models.Group
import com.heppihome.data.models.Task
import com.heppihome.data.models.User
import com.heppihome.ui.routes.tasks.CalendarView
import com.heppihome.ui.routes.tasks.HomeTasksScreen
import com.heppihome.ui.routes.tasks.UserSelection
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock

class TaskRouteTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testHomeTaskRoute(){
        val list = listOf(Task())
        val click = mock<()->Unit>()
        val taskclick = mock<(Task)->Unit>()
        composeTestRule.setContent {
            HomeTasksScreen(
                today = list,
                tomorrow = list,
                expanded = false,
                toggleDropDown = click,
                onAddTask = click,
                onChecked = taskclick,
                group = Group(),
                onBackPressed = click,
                onInvitePerson = click,
                isAdmin = true,
                click,
                click
            )
        }
        composeTestRule.onNodeWithContentDescription("add task button").assertIsDisplayed()
        // Tasks wordt al op een andere plaats getest
    }

    @Test
    fun testUserSelection() {
        val checkuser = mock<(User, Boolean) -> Unit>()
        composeTestRule.setContent {
            UserSelection(
                users = listOf(User()),
                selected = listOf("default"),
                onCheckUser = checkuser,
            )
        }
        // kijken of userselection getoond wordt
        val context: Context = InstrumentationRegistry.getInstrumentation().targetContext
        @StringRes val taskfor: String = context.resources.getString(R.string.TaskForPerson)
        composeTestRule.onNodeWithText(taskfor).assertIsDisplayed()
    }

    @Test
    fun testCalendarView(){
        val updatedate = mock<(Int, Int, Int)->Unit>()
        val updatetime = mock<(Int, Int)->Unit>()
        composeTestRule.setContent {
            CalendarView(cal = java.util.Calendar.getInstance(), hours = "0", date = "",
                updateDatepicker = updatedate, updateTimePicker = updatetime)
        }
        val context: Context = InstrumentationRegistry.getInstrumentation().targetContext
        @StringRes val opendp: String = context.resources.getString(R.string.OpenDatePicker)
        composeTestRule.onNodeWithText(opendp).assertIsDisplayed()
        composeTestRule.onNodeWithText(opendp).assertHasClickAction()
        @StringRes val sdate: String = context.resources.getString(R.string.SelectedDate)
        composeTestRule.onNodeWithText(sdate).assertIsDisplayed()

        @StringRes val opentp: String = context.resources.getString(R.string.OpenTimePicker)
        composeTestRule.onNodeWithText(opentp).assertIsDisplayed()
        composeTestRule.onNodeWithText(opentp).assertHasClickAction()
        composeTestRule.onNodeWithText(sdate).onSiblings().filterToOne(hasNoClickAction())
            .assertIsDisplayed()
    }
}