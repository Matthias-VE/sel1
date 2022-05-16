package com.heppihome.data.test

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.google.firebase.Timestamp
import com.heppihome.data.models.*
import com.heppihome.viewmodels.groups.HomeGroupViewModel
import com.heppihome.viewmodels.tasks.HomeTasksViewModel
import com.heppihome.viewmodels.test.PopulateViewModel
import java.util.*

class PopulateDB constructor(private val vM : PopulateViewModel
) {

    private val admin_id = "N8BRCYIwn2bTO52pANuONS3nvJk1"

    private val user = User("Admin Test", admin_id, "admin@telenet.be")

    private val testGroups = listOf(
        Group("test groep", "dit is een groep dus", listOf(admin_id), listOf(admin_id), "KXuXm9sRW43maz0Dbi2u"),
        Group("Tweede groep", "dit is ook een groep", listOf(admin_id), listOf(admin_id), "qADb3QuTcFwlYk1yQBOj"),
        Group("groepie groep", "alweer een groep, gekte", listOf(admin_id), listOf(admin_id),"TyEA0R9Jb1DPhsKBMRE0")
    )

    private val testPoints = listOf(
        Shop("KXuXm9sRW43maz0Dbi2u", 50),
        Shop("qADb3QuTcFwlYk1yQBOj", 100),
        Shop("TyEA0R9Jb1DPhsKBMRE0", 35)
    )

    private val testTasks = listOf(
        Task("Zet de plantjes buiten", false, Timestamp(Date()), listOf(admin_id)),
        Task("Doe eens gek", true, users = listOf(admin_id)),
        Task("Dit is een mooie en nogal lange taak", false, users = listOf(admin_id)),
        Task("Nog meer taken", false, users = listOf(admin_id)),
        Task("Sample Task", true, users = listOf(admin_id))
    )

    private val testShop = listOf(
        ShopItem(name = "Go to disneyland!", points = 200),
        ShopItem(name= "Get a cookie", points = 10),
        ShopItem(name = "New Nintendo Switch game", points = 80),
        ShopItem(name= "Test item", points = 5)
    )

    @Composable
    fun Populate( b : Boolean) {
        if (b) {
            vM.insertGroups(testGroups)
            vM.insertPoints(testPoints, admin_id)
            testGroups.forEach {
                vM.insertTasks(testTasks, it)
                vM.insertShopItems(testShop, it)
            }
        }
    }
}