package com.heppihome.routeTests

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.platform.app.InstrumentationRegistry
import com.heppihome.R
import com.heppihome.data.FirebaseDao
import com.heppihome.data.HomeRepository
import com.heppihome.data.models.Invite
import com.heppihome.ui.routes.groups.DetailInviteScreen
import com.heppihome.ui.routes.groups.HomeInvitePersonScreen
import com.heppihome.ui.routes.groups.HomeInvitesScreen
import com.heppihome.viewmodels.groups.HomeGroupViewModel
import org.hamcrest.core.AnyOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.kotlin.mock

class InvitesRoutesTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Mock
    private lateinit var vM : HomeGroupViewModel

    @Before
    fun setup(){
        val repository = Mockito.mock(HomeRepository(Mockito.mock(FirebaseDao::class.java))::class.java)
        vM = HomeGroupViewModel(repository)
    }

    //TopBar wordt al getest in TopBarTest

    @Test
    fun testDetailInvite(){
        val inviteclick = mock<(Invite)->Unit>()
        val click = mock<()->Unit>()
        val invite = Invite()
        composeTestRule.setContent{
            DetailInviteScreen(invite = invite, onAcceptInvite = inviteclick,
                onDeclineInvite = inviteclick, onBackPressed = click)
        }

        val context: Context = InstrumentationRegistry.getInstrumentation().targetContext
        @StringRes val accdec: String = context.resources.getString(R.string.AcceptOrDeclineInvite)
        composeTestRule.onNodeWithText(accdec).assertIsDisplayed()

        @StringRes val acc: String = context.resources.getString(R.string.Accept)
        composeTestRule.onNodeWithText(acc).assertIsDisplayed()
        composeTestRule.onNodeWithText(acc).assertHasClickAction()
        @StringRes val dec: String = context.resources.getString(R.string.Decline)
        composeTestRule.onNodeWithText(dec).assertIsDisplayed()
        composeTestRule.onNodeWithText(dec).assertHasClickAction()
    }

    @Test
    fun testHomeInvitePerson(){
        //InputField wordt al op een andere plaats getest
        val click = mock<()->Unit>()
        val emailclick = mock<(String)->Unit>()
        composeTestRule.setContent {
            HomeInvitePersonScreen(email = "test", onBackPressed = click,
                onEmailChanged = emailclick, onPersonInvited = click)
        }
        val context: Context = InstrumentationRegistry.getInstrumentation().targetContext
        @StringRes val invite: String = context.resources.getString(R.string.InvitePerson)
        composeTestRule.onNodeWithText(invite).assertIsDisplayed()
    }

    @Test
    fun testHomeInviteEmpty(){
        val inviteclick = mock<(Invite)->Unit>()
        val click = mock<()->Unit>()
        val invites = emptyList<Invite>()
        composeTestRule.setContent {
            HomeInvitesScreen(
                invites = invites,
                onInviteClicked = inviteclick,
                onDeclineInviteClicked = inviteclick,
                onAcceptInviteClicked = inviteclick,
                onBackPressed = click
            )
        }
        val context: Context = InstrumentationRegistry.getInstrumentation().targetContext
        @StringRes val invite: String = context.resources.getString(R.string.NoInvites)
        composeTestRule.onNodeWithText(invite).assertIsDisplayed()
    }

    @Test
    fun testHomeInvite(){
        val inviteclick = mock<(Invite)->Unit>()
        val click = mock<()->Unit>()
        val invites = listOf(Invite())
        composeTestRule.setContent {
            HomeInvitesScreen(
                invites = invites,
                onInviteClicked = inviteclick,
                onDeclineInviteClicked = inviteclick,
                onAcceptInviteClicked = inviteclick,
                onBackPressed = click
            )
        }
        // controleer of SideView is opgeroepen
        composeTestRule.onNodeWithContentDescription("Accept").assertIsDisplayed()
    }
    // SideView is hetzelfde geïmplementeerd, dus niet echt nuttig om nog eens te testen
}