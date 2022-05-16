package com.heppihome

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.heppihome.data.HomeRepository
import com.heppihome.data.models.Invite
import com.heppihome.viewmodels.groups.InvitesViewModel
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

class InvitesViewModelTest {

    @get:Rule
    @Suppress("unused")
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private val mockRepository: HomeRepository = mock()

    private val viewModel = InvitesViewModel(mockRepository)

    @Test
    suspend fun acceptInvite(){
        val invite = Invite()
        viewModel.acceptInvite(invite)
        verify(mockRepository).acceptInvite(invite)
        verify(mockRepository).getAllInvites()
    }

    @Test
    suspend fun removeInvite(){
        val invite = Invite()
        viewModel.removeInviteFromList(invite)
        verify(mockRepository).declineInvite(invite)
        verify(mockRepository).getAllInvites()
    }
}