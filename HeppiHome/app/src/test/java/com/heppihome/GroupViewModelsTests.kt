package com.heppihome

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.heppihome.data.HomeRepository
import com.heppihome.data.models.Group
import com.heppihome.viewmodels.groups.AddGroupViewModel
import com.heppihome.viewmodels.groups.EditGroupViewModel
import com.heppihome.viewmodels.groups.InvitePersonViewModel
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

class GroupViewModelsTests {

    @get:Rule
    @Suppress("unused")
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private val mockRepository: HomeRepository = mock()

    @Test
    suspend fun invitePersonView(){
        val viewModel = InvitePersonViewModel(mockRepository)
        val group = Group()
        viewModel.invitePerson()
        verify(mockRepository).sendInviteTo("", group)
    }

    @Test
    fun editGroupView(){
        val viewModel = EditGroupViewModel(mockRepository)
        val group = Group()
        val context : Context = mock()
        viewModel.editGroup(group, context)
        verify(mockRepository).editGroup(group, "newname", "newDesc")
    }

    @Test
    fun addGroupView(){
        val viewModel = AddGroupViewModel(mockRepository)
        val context : Context = mock()
        viewModel.addGroups(context)
        verify(mockRepository).addGroup(Group())
    }
}