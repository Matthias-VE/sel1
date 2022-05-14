package com.heppihome.ui.routes.shop

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.heppihome.data.models.Group
import com.heppihome.data.models.ShopItem
import com.heppihome.ui.components.ConfirmDialog
import com.heppihome.ui.components.Topbar
import com.heppihome.viewmodels.shop.HomeInventoryViewModel

@Composable
fun HomeInventoryRoute(
    vM : HomeInventoryViewModel,
    onBackPressed : () -> Unit
){
    vM.refreshInventory()

    val items by vM.inventory.collectAsState()

    HomeInventoryScreen(
        onBackPressed,
        items,
        vM::cashInReward
    )
}

@Composable
fun HomeInventoryScreen(
    onBackPressed: () -> Unit,
    inventoryItems : List<ShopItem>,
    onClickItem: (ShopItem) -> Unit

) {
    Column() {
        Topbar(title = "Inventory", onBackPressed)
        Spacer(modifier = Modifier.padding(vertical = 15.dp))
        InventoryList(
            inventoryItems = inventoryItems,
            onClickItem
        )
    }
}

@Composable
fun InventoryList(
    inventoryItems: List<ShopItem>,
    onClickItem : (ShopItem) -> Unit
) {
    LazyColumn(modifier = Modifier
        .fillMaxHeight()
        .padding(16.dp)
        ,
        verticalArrangement = Arrangement.spacedBy(25.dp)
    ) {
        items(inventoryItems) {
            InventoryItem(si = it, onClick = onClickItem)
        }
    }
}

@Composable
fun InventoryItem(si : ShopItem, onClick : (ShopItem) -> Unit) {

    var showCashInConfirm by remember { mutableStateOf(false) }

    if (showCashInConfirm) {
        ConfirmDialog(content = "Do you want to cash in this reward?",
            onDismiss = { showCashInConfirm = false},
            onConfirm = {
                showCashInConfirm = false
                onClick(si)
            }
        )
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colors.secondary, shape = RoundedCornerShape(8.dp))
            .padding(12.dp)
            .clickable(enabled = true, onClick = { showCashInConfirm = true })
        ,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(Modifier.weight(0.8F)) {
            Text(si.name)
        }
        Text(
            "Cash in", style = MaterialTheme.typography.button,
            textAlign = TextAlign.Center,
            modifier =
            Modifier
                .weight(0.2F)
                .background(Color.White, shape = RoundedCornerShape(8.dp))
        )


    }

}