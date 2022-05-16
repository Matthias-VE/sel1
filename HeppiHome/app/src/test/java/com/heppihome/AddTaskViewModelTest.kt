package com.heppihome

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.heppihome.data.HomeRepository
import com.heppihome.data.models.Group
import com.heppihome.data.models.Task
import com.heppihome.viewmodels.tasks.AddTaskViewModel
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

class AddTaskViewModelTest {

    @get:Rule
    @Suppress("unused")
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private val mockRepository: HomeRepository = mock()

    private val viewModel = AddTaskViewModel(mockRepository)

    @Test
    fun addTask(){
        val g = Group()
        viewModel.addTask()
        verify(mockRepository).addTask(Task(), g)
    }

}