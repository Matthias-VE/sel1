package com.heppihome.viewmodels.shop

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.heppihome.Util.NumberUtil
import com.heppihome.data.HomeRepository
import com.heppihome.data.models.ResultState
import com.heppihome.data.models.ShopItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditShopItemViewModel @Inject constructor(private val rep : HomeRepository) : ViewModel() {

    private var id = ""
    private val _name = MutableStateFlow("")
    private val _points = MutableStateFlow("")

    val name = _name.asStateFlow()
    val points = _points.asStateFlow()

    fun setItem(shopItem : ShopItem) {
        id = shopItem.id
        _name.value = shopItem.name
        _points.value = shopItem.points.toString()
    }

    fun editItem(context: Context) {
        viewModelScope.launch {
            rep.editShopItemInShop(sitem = ShopItem(id = id,
                name = _name.value, points = NumberUtil.verifyInput(_points.value)
            )).collect {
                when(it) {
                    is ResultState.Success -> setToast("Reward added succesfully", context, Toast.LENGTH_SHORT) // Do something when succes
                    is ResultState.Loading -> setToast("Processing...", context, Toast.LENGTH_SHORT) // Do something while loading
                    is ResultState.Failed -> setToast("Something went wrong\nPlease try again", context, Toast.LENGTH_SHORT) // Do something when failed
                }
            }
        }
    }

    fun updatePoints(string: String) {
        _points.value = string
    }

    fun updateName(string : String) {
        _name.value = string
    }

    private fun setToast(str : String, c : Context, duration : Int) {
        Toast.makeText(c, str, duration).show()
    }

    fun isValid(context: Context): Boolean {
        if (_name.value.isEmpty() || _points.value.isEmpty()) {
            Toast.makeText(context, "Please fill in both fields", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }


}