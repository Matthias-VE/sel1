package com.heppihome.viewmodels.groups

import android.content.Context
import android.widget.Toast
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.heppihome.data.HomeRepository
import com.heppihome.data.models.Group
import com.heppihome.data.models.ResultState
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

    private var _toastMessage : MutableStateFlow<String> = MutableStateFlow("")
    var toastMessage : StateFlow<String> = _toastMessage


    fun setGroup(newName : String) {
        _groupName.value = TextFieldValue(newName)
    }

    fun setDescription(newDes : String) {
        _description.value = TextFieldValue(newDes)
    }

    fun setToast(newToast : String) {
        _toastMessage.value = newToast
    }

    fun addGroups(context: Context) {
        val toAdd = Group(_groupName.value.text, _description.value.text, listOf(rep.user.id))


        viewModelScope.launch {
            rep.addGroup(toAdd).collect {
                when(it) {
                    is ResultState.Success -> setToast("Group added succesfully") // Do something when succes
                    is ResultState.Loading -> setToast("Loading...") // Do something while loading
                    is ResultState.Failed -> setToast("Something went wrong\nPlease try again") // Do something when failed
                }
            }

            Toast.makeText(context, _toastMessage.value, Toast.LENGTH_LONG).show()
            setGroup("")
            setDescription("")
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
