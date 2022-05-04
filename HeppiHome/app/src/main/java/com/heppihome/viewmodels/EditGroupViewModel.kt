package com.heppihome.viewmodels

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import com.heppihome.data.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class EditGroupViewModel @Inject constructor(private val rep : HomeRepository) : ViewModel() {
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


}