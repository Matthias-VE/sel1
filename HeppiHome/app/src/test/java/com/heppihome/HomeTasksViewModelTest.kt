package com.heppihome

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.heppihome.data.HomeRepository
import com.heppihome.data.models.Group
import com.heppihome.data.models.Task
import com.heppihome.viewmodels.tasks.HomeTasksViewModel
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

class HomeTasksViewModelTest {

    @get:Rule
    @Suppress("unused")
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private val mockRepository: HomeRepository = mock()
    private val viewModel = HomeTasksViewModel(mockRepository)

    @Test
    fun goBack(){
        viewModel.onGoBack()
        verify(mockRepository).removeListeners()
    }

    @Test
    fun toggleTask(){
        val task = Task()
        viewModel.toggleTask(task)
        verify(mockRepository).checkTask(task, Group())
    }

    @Test
    fun checkTask(){
        val task = Task()
        viewModel.toggleTask(task)
        verify(mockRepository).checkTask(task)
    }
}