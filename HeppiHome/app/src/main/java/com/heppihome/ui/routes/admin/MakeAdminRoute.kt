package com.heppihome.ui.routes

import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Checkbox
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.heppihome.R
import com.heppihome.data.models.User
import com.heppihome.ui.components.ConfirmDialog
import com.heppihome.ui.components.Topbar
import com.heppihome.viewmodels.admin.MakeAdminViewModel

@Composable
fun MakeAdminRoute(
    vM : MakeAdminViewModel = hiltViewModel(),
    onBackPressed: () -> Unit
) {
    vM.usersInGroup()

    val feedback by vM.feedback.collectAsState()
    val users by vM.users.collectAsState()
    val admins by vM.admins.collectAsState()

    if (feedback) {
        Toast.makeText(LocalContext.current, stringResource(R.string.MadeAdmin), Toast.LENGTH_SHORT).show()
        vM.resetFeedback()
        onBackPressed()
    }

    MakeAdminScreen(
        onBackPressed = onBackPressed,
        onMakeAdmin = vM::makeAdmin,
        users,
        admins
    )

}

@Composable
fun MakeAdminScreen(
    onBackPressed : () -> Unit,
    onMakeAdmin : (User) -> Unit,
    users : List<User>,
    admins : List<String>
) {
    Column() {
        Topbar(title = stringResource(R.string.MakeAdmin), onBackPressed = onBackPressed)
        Spacer(modifier = Modifier.padding(15.dp))
        AdminSelection(users = users, admins = admins, onMakeAdmin = onMakeAdmin)
    }
}

@Composable
fun AdminSelection(users : List<User>, admins : List<String>, onMakeAdmin : (User) -> Unit) {


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(25.dp)
    ) {
        Text(stringResource(R.string.ChooseWhoAdmin), fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.padding(5.dp))
        for (user in users) {

            var showAdminConfirm by remember {mutableStateOf(false)}

            if (user.id !in admins) {
                Row(
                    modifier = Modifier
                        .clickable(true, onClick = { showAdminConfirm = true })
                        .background(MaterialTheme.colors.secondary, RoundedCornerShape(8.dp))
                        .padding(5.dp)
                ) {

                    if (showAdminConfirm) {
                        ConfirmDialog(content = stringResource(R.string.MakeAdmin, user.name),
                            onDismiss = { showAdminConfirm = false},
                            onConfirm = {
                                showAdminConfirm = false
                                onMakeAdmin(user)
                            }
                        )
                    }

                    Text(
                        text = user.name,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}


@Preview
@Composable
fun MakeAdminRoutePreview() {

    val userss = mutableListOf<User>()
    val adminss = mutableListOf<String>()
    for (i in 0..10) {
        val u = User("user number$i", "user$i@telenet.be", "idnumbero$i")
        userss.add(u)
        if (i % 2 == 0) {
            adminss.add(u.id)
        }
    }

    MakeAdminScreen(
        {},
        {},
        userss,
        adminss
    )
}
