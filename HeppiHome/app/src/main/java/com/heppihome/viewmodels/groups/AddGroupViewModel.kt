package com.heppihome.viewmodels.groups

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.heppihome.data.HomeRepository
import com.heppihome.data.models.Group
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddGroupViewModel @Inject constructor(private val rep : HomeRepository) : ViewModel() {

    private var _groupName : MutableStateFlow<TextFieldValue> = MutableStateFlow(TextFieldValue(""))
    var groupName : StateFlow<TextFieldValue> = _groupName

    private var _description : MutableStateFlow<TextFieldValue> = MutableStateFlow(TextFieldValue(""))
    var description : StateFlow<TextFieldValue> = _description

    fun setGroup(newName : String) {
        _groupName.value = TextFieldValue(newName)
    }

    fun setDescription(newDes : String) {
        _description.value = TextFieldValue(newDes)
    }

    fun addGroups() {
        viewModelScope.launch {
            val toAdd = Group(_groupName.value.text, _description.value.text, listOf(rep.user.id))
            //println(_groupName.value.text)
            rep.addGroup(toAdd).collect()
            setGroup("")
            setDescription("")

        }
    }



}
