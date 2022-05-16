package com.heppihome.ui.routes.shop

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.heppihome.R
import com.heppihome.data.models.ShopItem

import com.heppihome.ui.components.InputField
import com.heppihome.ui.components.InputNumberField
import com.heppihome.ui.components.Topbar
import com.heppihome.viewmodels.shop.EditShopItemViewModel

@Composable
fun EditShopItemRoute(
    vM : EditShopItemViewModel = hiltViewModel(),
    goBackToShop : () -> Unit,
    shopItem : ShopItem
) {

    LaunchedEffect(true) {
        vM.setItem(shopItem)
    }

    val name by vM.name.collectAsState()
    val points by vM.points.collectAsState()

    EditShopItemScreen(
        goBackToShop,
        name,
        points,
        vM::updateName,
        vM::updatePoints,
        vM::isValid,
        vM::editItem
    )

}
@Composable
fun EditShopItemScreen(
    onCancel : () -> Unit,
    name : String,
    points : String,
    changeName : (String) -> Unit,
    changePoints : (String) -> Unit,
    isValid : (Context) -> Boolean,
    editReward : (Context) -> Unit
) {
    val context = LocalContext.current
    Column() {
        Topbar(stringResource(R.string.EditReward), onCancel)
        Column(modifier = Modifier
            .padding(10.dp)) {
            InputField(name = stringResource(R.string.RewardName), description = name, changeName)
            InputNumberField(name = stringResource(R.string.NumberOfPoints), value = points, changePoints)
            Button(onClick = {
                if (isValid(context)) {
                    editReward(context)
                    onCancel()
                }
            },
                modifier = Modifier.padding(10.dp)) {
                Text(stringResource(R.string.Edit))
            }
        }
    }
}

@Preview
@Composable
fun EditShopItemRoutePreview() {

    var name by remember { mutableStateOf("")}
    var points by remember { mutableStateOf("")}
    fun changeName(str : String) {name = str}
    fun changePoints(str: String) {points = str}
    fun isValid(c : Context): Boolean {return true}

    AddShopItemScreen(
        onCancel = {},
        name = name,
        points = points,
        changeName = ::changeName,
        changePoints = ::changePoints,
        isValid = ::isValid,
        addReward = {}
    )

}