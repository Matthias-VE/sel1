package com.heppihome.viewmodels.groups

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.heppihome.data.HomeRepository
import com.heppihome.data.models.Group
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditGroupViewModel @Inject constructor(private val rep : HomeRepository) : ViewModel() {
    private val _groupName : MutableStateFlow<TextFieldValue> = MutableStateFlow(TextFieldValue(""))
    val groupName : StateFlow<TextFieldValue> = _groupName

    private val _description : MutableStateFlow<TextFieldValue> = MutableStateFlow(TextFieldValue(""))
    val description : StateFlow<TextFieldValue> = _description

    private var group : Group = Group();

    fun setGroup(newGroup : Group) {
        group = newGroup
        setName(group.name)
        setDescription(group.description)
    }

    fun setName(newName : String) {
        _groupName.value = TextFieldValue(newName)
    }

    fun setDescription(newDes : String) {
        _description.value = TextFieldValue(newDes)
    }

    fun updateGroup(g : Group) {
        viewModelScope.launch {  }
    }


}