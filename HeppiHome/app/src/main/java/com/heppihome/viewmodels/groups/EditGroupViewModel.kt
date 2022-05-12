package com.heppihome.viewmodels.groups

import android.content.Context
import android.widget.Toast
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

    private var _toastMessage : MutableStateFlow<String> = MutableStateFlow("")
    var toastMessage : StateFlow<String> = _toastMessage


    fun setName(newName : String) {
        _groupName.value = TextFieldValue(newName)
    }

    fun setDescription(newDes : String) {
        _description.value = TextFieldValue(newDes)
    }

    fun setToast(newToast : String, context: Context) {
        _toastMessage.value = newToast
        Toast.makeText(context, _toastMessage.value, Toast.LENGTH_LONG).show()
    }

    fun editGroup(g : Group, context: Context) {
        viewModelScope.launch {
            rep.editGroup(g, _groupName.value.text, _description.value.text).collect {
                when(it) {
                    is ResultState.Success -> setToast("Group edited succesfully", context) // Do something when succes
                    is ResultState.Loading -> setToast("Processing...", context) // Do something while loading
                    is ResultState.Failed -> setToast("Something went wrong\nPlease try again", context) // Do something when failed
                }
            }
        }
    }

    fun isValid(context: Context): Boolean {
        if (_groupName.value.text.isEmpty() || _description.value.text.isEmpty()) {
            Toast.makeText(context, "Please fill in both fields", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }
}