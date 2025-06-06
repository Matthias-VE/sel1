package com.heppihome

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.heppihome.data.HomeRepository
import com.heppihome.data.models.Group
import com.heppihome.viewmodels.groups.HomeGroupViewModel
import org.junit.Rule
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

class HomeGroupViewModelTest {

    @get:Rule
    @Suppress("unused")
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private val mockRepository: HomeRepository = mock()

    private val viewModel = HomeGroupViewModel(mockRepository)

    @Test
    suspend fun refreshInvites(){
        viewModel.refreshInvites()
        verify(mockRepository).getAllInvites()
    }

    @Test
    suspend fun refreshGroups(){
        viewModel.refreshGroups()
        verify(mockRepository).getAllGroups()
    }

    @org.junit.Test
    fun leaveGroup(){
        val group = Group()
        val context : Context = mock()
        viewModel.leaveGroup(group, context)
        verify(mockRepository).leaveGroup(group)
    }
}