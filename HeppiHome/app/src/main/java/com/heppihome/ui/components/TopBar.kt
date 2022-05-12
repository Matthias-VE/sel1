package com.heppihome.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.heppihome.R
import com.heppihome.data.models.Group

@Composable
fun
Topbar(title : String,
       onBackPressed: () -> Unit) {
    Surface(modifier = Modifier.fillMaxWidth(), color = MaterialTheme.colors.primary) {
        Row(modifier = Modifier
            .fillMaxWidth()) {
            Row(modifier = Modifier.padding(10.dp), horizontalArrangement = Arrangement.Start) {
                IconButton(onClick =  onBackPressed) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Return", modifier = Modifier.size(40.dp))
                }
            }
            Row(modifier = Modifier.padding(10.dp)) {
                Text(title, fontSize = 30.sp, style = MaterialTheme.typography.h6)
            }
        }
    }
}

@Composable
fun TopbarNoBackArrow(title : String) {
    Surface(modifier = Modifier.fillMaxWidth(), color = MaterialTheme.colors.primary) {
        Row(modifier = Modifier
            .fillMaxWidth()) {
            Row(modifier = Modifier.padding(10.dp), horizontalArrangement = Arrangement.Start) {
                Spacer(Modifier.padding(horizontal = 10.dp))
                Text(title, fontSize = 30.sp, style = MaterialTheme.typography.h6)
            }
        }
    }
}

@Composable
fun TopbarWithOptionsNoBackArrow(title : String, expanded: Boolean, toggle: () -> Unit,
                                 itemStrings : List<String>,
                                 itemOnClicks : List<() -> Unit>) {

    Surface(modifier = Modifier.fillMaxWidth(), color = MaterialTheme.colors.primary) {
        Row(modifier = Modifier
            .fillMaxWidth()) {
            Row(modifier = Modifier.padding(10.dp), horizontalArrangement = Arrangement.Start) {
                Spacer(modifier = Modifier.padding(horizontal = 10.dp))
                Text(title, fontSize = 30.sp, style = MaterialTheme.typography.h6)
            }
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp), horizontalArrangement = Arrangement.End) {
                IconButton(onClick = toggle) {
                    Icon(
                        Icons.Default.MoreVert,
                        contentDescription = "Options",
                        modifier = Modifier.size(40.dp)
                    )
                    DropDownMenuAsOptions( expanded, toggle, itemStrings, itemOnClicks)
                }
            }
        }
    }
}

@Composable
fun TopbarWithSettings(title: String, onSettingsPressed: () -> Unit) {
    Surface(modifier = Modifier.fillMaxWidth(), color = MaterialTheme.colors.primary) {
        Row(modifier = Modifier.padding(10.dp)) {
            Text(title, fontSize = 30.sp, style = MaterialTheme.typography.h6)
        }
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp), horizontalArrangement = Arrangement.End) {
            IconButton(onClick =  onSettingsPressed) {
                Icon(Icons.Default.Settings, contentDescription = "Settings", modifier = Modifier.size(40.dp))
            }
        }
    }
}

@Composable
fun TopbarWithOptions(title : String, expanded: Boolean, toggle: () -> Unit,
                      onBackPressed: () -> Unit, itemStrings : List<String>,
                      itemOnClicks : List<() -> Unit>
){

    Surface(modifier = Modifier.fillMaxWidth(), color = MaterialTheme.colors.primary) {
        Row(modifier = Modifier
            .fillMaxWidth()) {
            Row(modifier = Modifier.padding(10.dp), horizontalArrangement = Arrangement.Start) {
                IconButton(onClick =  onBackPressed) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Return", modifier = Modifier.size(40.dp))
                }
            }
            Row(modifier = Modifier.padding(10.dp)) {
                Text(title, fontSize = 30.sp, style = MaterialTheme.typography.h6)
            }
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp), horizontalArrangement = Arrangement.End) {
                IconButton(onClick = toggle) {
                    Icon(
                        Icons.Default.MoreVert,
                        contentDescription = "Options",
                        modifier = Modifier.size(40.dp)
                    )
                    DropDownMenuAsOptions( expanded, toggle, itemStrings, itemOnClicks)
                }
            }
        }
    }
}

@Composable
fun DropDownMenuAsOptions(expanded : Boolean, toggle : () -> Unit,
                          itemStrings: List<String>, itemOnClicks: List<() -> Unit>
) {
    var n = itemStrings.size
    if (itemOnClicks.size < n) n = itemOnClicks.size
    DropdownMenu(expanded = expanded, onDismissRequest = toggle) {
        for (i in 0 until n) {
            DropdownMenuItem(onClick = itemOnClicks[i]) {
                Text(itemStrings[i])
            }
        }
    }
}