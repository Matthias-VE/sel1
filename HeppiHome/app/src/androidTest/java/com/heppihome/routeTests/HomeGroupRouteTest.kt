package com.heppihome.routeTests

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.platform.app.InstrumentationRegistry
import com.heppihome.R
import com.heppihome.data.FirebaseDao
import com.heppihome.data.HomeRepository
import com.heppihome.data.models.Group
import com.heppihome.ui.routes.groups.HomeGroupScreen
import com.heppihome.ui.routes.groups.SideView
import com.heppihome.viewmodels.groups.HomeGroupViewModel
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.kotlin.atLeastOnce
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

class HomeGroupRouteTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Mock
    private lateinit var vM : HomeGroupViewModel

    @Before
    fun setup(){
        val repository = Mockito.mock(HomeRepository(Mockito.mock(FirebaseDao::class.java))::class.java)
        vM = HomeGroupViewModel(repository)
    }

    @Test
    fun testHeader() {
        val groupclick = mock<(Group) -> Unit>()
        val click = mock<() -> Unit>()
        val groups = listOf(Group())

        composeTestRule.setContent {
            HomeGroupScreen(
                groups = groups,
                vM = vM,
                onGroupClicked = groupclick,
                onNewGroupClicked = click,
                onEditGroupClicked = groupclick,
                expanded = false,
                onInvitesClicked = click,
                toggle = click
            )
        }
        val context: Context = InstrumentationRegistry.getInstrumentation().targetContext
        @StringRes val label: String = context.resources.getString(R.string.Groups)
        composeTestRule.onNodeWithText(label).onParent().assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Options").performClick()
        verify(click, atLeastOnce())()
    }

    @Test
    fun testDropDown() {
        val groupclick = mock<(Group) -> Unit>()
        val click = mock<() -> Unit>()
        val groups = listOf(Group())
        composeTestRule.setContent {
            HomeGroupScreen(
                groups = groups,
                onGroupClicked = groupclick,
                expanded = true,
                toggle = click,
                onNewGroupClicked = click,
                vM = vM,
                onEditGroupClicked = groupclick,
                onInvitesClicked = click,
            )
        }
        val context: Context = InstrumentationRegistry.getInstrumentation().targetContext
        @StringRes val newgroup: String = context.resources.getString(R.string.NewGroup)
        composeTestRule.onNodeWithText(newgroup).assertIsDisplayed()
        @StringRes val joingroup: String = context.resources.getString(R.string.JoinGroup)
        composeTestRule.onNodeWithText(joingroup).assertIsDisplayed()
        @StringRes val leavegroup: String = context.resources.getString(R.string.LeaveGroup)
        composeTestRule.onNodeWithText(leavegroup).assertIsDisplayed()
    }

    @Test
    fun testSideView(){
        val groupclick = mock<(Group) -> Unit>()
        val click = mock<() -> Unit>()
        val group = Group()
        composeTestRule.setContent{
            SideView(g = group, onGroupClicked = groupclick, vM = vM,
                onEditGroupClicked = groupclick)
        }
        composeTestRule.onNodeWithContentDescription("Edit").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Delete").assertIsDisplayed()
        composeTestRule.onNodeWithText(group.description).assertIsDisplayed()
        composeTestRule.onNodeWithText(group.name).assertIsDisplayed()
    }
}