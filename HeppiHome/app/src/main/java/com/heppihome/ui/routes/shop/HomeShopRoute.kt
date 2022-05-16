package com.heppihome.ui.routes.shop

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.heppihome.R
import com.heppihome.data.models.Group
import com.heppihome.data.models.ShopItem
import com.heppihome.ui.components.TopbarNoBackArrow
import com.heppihome.ui.components.TopbarWithIcon
import com.heppihome.viewmodels.shop.HomeShopViewModel

@Composable
fun HomeShopRoute(
    vM : HomeShopViewModel = hiltViewModel(),
    goToItemDetail : (ShopItem) -> Unit,
    goToInventory : () -> Unit,
    goToAddShopItem : () -> Unit,
){
    vM.setListener()
    vM.refreshItems()

    val points by vM.points.collectAsState()
    val items by vM.shopItems.collectAsState()
    val isAdmin by vM.isAdmin.collectAsState()
    val buySuccess by vM.buySuccess.collectAsState()

    HomeShopScreen(
        goToInventory,
        buySuccess,
        vM::resetSuccess,
        points = points.points,
        items,
        vM::buyItem,
        isAdmin,
        goToItemDetail,
        goToAddShopItem
    )
}

@Composable
fun WithAddButton(onClickAdd : () -> Unit, content : @Composable (PaddingValues) -> Unit) {
    Scaffold(
        floatingActionButton = {FloatingActionButton(onClick = onClickAdd)
            {Icon(Icons.Default.Add, "add ShopItem button")}
        },
        floatingActionButtonPosition = FabPosition.End,
        content = content
    )
}

@Composable
fun HomeShopScreen(
    goToInventory : () -> Unit,
    buySuccess : Pair<Boolean, Boolean>,
    reset : () -> Unit,
    points : Int,
    items : List<ShopItem>,
    onBuyItem: (ShopItem) -> Boolean,
    isAdmin: Boolean,
    onClickItem: (ShopItem) -> Unit,
    onAddItem: () -> Unit,

) {
    if (buySuccess.first && buySuccess.second) {
        Toast.makeText(LocalContext.current, stringResource(R.string.PurchaseSuccessful), Toast.LENGTH_SHORT).show()
        reset()
    }

    val content : @Composable (PaddingValues) -> Unit = {
        Column(
            modifier = Modifier.padding(it)
        ) {
            TopbarWithIcon(title = stringResource(R.string.Shop), Icons.Filled.List, "Inventory", goToInventory)
            PointsDisplay(points = points)
            Spacer(modifier = Modifier.height(15.dp))
            ShopItems(
                l = items,
                onBuyItem = onBuyItem,
                clickable = isAdmin,
                onClickItem = onClickItem
            )
        }
    }

    if (isAdmin) {
        WithAddButton(onClickAdd = onAddItem, content = content)
    } else {
        Scaffold(content = content)
    }

}

@Composable
fun PointsDisplay(points : Int) {

    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(10.dp),) {
        Text(
                    stringResource(R.string.YouCurrentlyHave),
            style = MaterialTheme.typography.body1,
            modifier = Modifier.weight(0.8F)
        )
        Row(modifier = Modifier.weight(0.2F)) {
            Text(
                "$points",
                color = Color.Black,
                fontFamily = FontFamily(Font(R.font.partyconfetti_regular, FontWeight.Normal)),
                fontWeight = FontWeight.Normal,
                fontSize = 25.sp
            )
            Spacer(Modifier.padding(horizontal = 5.dp))
            Text(
                stringResource(R.string.PointsShort),
                fontFamily = FontFamily(Font(R.font.partyconfetti_regular, FontWeight.Normal)),
                fontWeight = FontWeight.Normal,
                fontSize = 15.sp,
                modifier = Modifier.padding(vertical = 5.dp)
            )
        }
    }
}


@Composable
fun ShopItems(
    l : List<ShopItem>,
    onBuyItem : (ShopItem) -> Boolean,
    clickable : Boolean,
    onClickItem : (ShopItem) -> Unit
){
    LazyColumn(modifier = Modifier
        .fillMaxHeight()
        .padding(16.dp)
        ,
        verticalArrangement = Arrangement.spacedBy(25.dp)
    ) {
        items(l) {
            ListShopItem(item = it, onBuyItem = onBuyItem, clickable = clickable, onClickItem = onClickItem)
        }
    }
}


@Composable
fun ListShopItem(
    item : ShopItem,
    onBuyItem: (ShopItem) -> Boolean,
    clickable: Boolean,
    onClickItem: (ShopItem) -> Unit
){
    val context = LocalContext.current
    val txt = stringResource(R.string.NotEnoughPoints)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colors.secondary, shape = RoundedCornerShape(8.dp))
            .padding(12.dp)
            .clickable(enabled = clickable, onClick = { onClickItem(item) })
        ,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(Modifier.weight(0.8F)) {
            Text(item.name)
        }
        Text(
            "${item.points}" + stringResource(R.string.PtsBuy), style = MaterialTheme.typography.button,
            textAlign = TextAlign.Center,
            modifier =
            Modifier
                .clickable(true, onClick =
                {
                    val str = onBuyItem(item)
                    if (!str) {
                        Toast.makeText(
                            context,
                            txt,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                )
                .weight(0.2F)
                .background(Color.White, shape = RoundedCornerShape(8.dp))
        )


    }

}

@Preview
@Composable
fun HomeShopRoutePreview() {
    HomeShopScreen(
        {},
        Pair(false, false),
        {},
        points = 100,
        items = listOf(
            ShopItem(name = "Go to disneyland!", points = 200),
            ShopItem(name= "Get a cookie", points = 10),
            ShopItem(name = "New Nintendo Switch game", points = 80),
            ShopItem(name= "Test item", points = 5)
        ),
        onBuyItem = {true},
        isAdmin = true,
        onClickItem = {},
        {}
    )
}