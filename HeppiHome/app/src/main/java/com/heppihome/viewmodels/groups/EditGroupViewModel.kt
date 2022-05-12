package com.heppihome.viewmodels.groups

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.heppihome.data.HomeRepository
import com.heppihome.data.models.Group
import com.heppihome.data.models.ResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditGroupViewModel @Inject constructor(private val rep : HomeRepository) : ViewModel() {
    private val _groupName : MutableStateFlow<TextFieldValue> = MutableStateFlow(TextFieldValue(""))
    val groupName : StateFlow<TextFieldValue> = _groupName

    private val _description : MutableStateFlow<TextFieldValue> = MutableStateFlow(TextFieldValue(""))
    val description : StateFlow<TextFieldValue> = _description

    private val _status : MutableStateFlow<ResultState<String>> = MutableStateFlow(ResultState.waiting())
    val status = _status.asStateFlow()

    fun setName(newName : String) {
        _groupName.value = TextFieldValue(newName)
    }

    fun setDescription(newDes : String) {
        _description.value = TextFieldValue(newDes)
    }

    fun editGroup(g : Group) {
        viewModelScope.launch {
            rep.editGroup(g, _groupName.value.text, _description.value.text).collect {
                _status.value = it
            }
        }
    }
}