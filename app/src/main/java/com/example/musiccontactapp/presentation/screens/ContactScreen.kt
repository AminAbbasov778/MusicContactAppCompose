package com.example.musiccontactapp.presentation.screens

import android.Manifest.permission
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import com.example.musiccontactapp.R
import com.example.musiccontactapp.presentation.intents.ContactIntent
import com.example.musiccontactapp.presentation.states.ContactEffect
import com.example.musiccontactapp.presentation.viewmodels.ContactViewModel
import com.example.musiccontactapp.ui.theme.Btn
import com.example.musiccontactapp.ui.theme.DarkGrey
import com.example.musiccontactapp.ui.theme.Grey
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

@SuppressLint("UnusedBoxWithConstraintsScope")
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ContactScreen(viewModel: ContactViewModel) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsState()
    val permissionState = rememberPermissionState(permission.READ_CONTACTS)
    val contactsList = if(state.searchInput.isNotBlank()) state.searchResult else state.contacts

    LaunchedEffect(permissionState.status) {
        if (permissionState.status.isGranted) {
            viewModel.contactIntents(ContactIntent.GetContacts)
        } else {
            permissionState.launchPermissionRequest()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collect {
           if(it.openDialer.isNotEmpty()){
               val intent = Intent(Intent.ACTION_DIAL).apply {
                   data = Uri.parse("tel:${it.openDialer}")


               }
               context.startActivity(intent)
           }
        }

    }

    BoxWithConstraints(
        modifier = Modifier.fillMaxSize()
    ) {
        val density = LocalDensity.current
        val maxRadius = with(density) { (maxWidth.coerceAtLeast(maxHeight) / 2f).toPx() }
        val centerOffset = with(density) { Offset(maxWidth.toPx() / 12f, maxHeight.toPx() / 2f) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.radialGradient(
                        0f to Color(0xFF781ECF),
                        0.5f to Color(0xFF3B1D5E),
                        1f to Color.Black,
                        center = centerOffset,
                        radius = maxRadius
                    )
                )
        ) {

            Text(
                text = "Welcome back!",
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                color = Color.White,
                modifier = Modifier.padding(top = 100.dp, start = 15.dp)
            )

            Text(
                text = "Who do you want to call today?",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Grey,
                modifier = Modifier.padding(top = 10.dp, start = 15.dp)
            )

            TextField(
                value = state.searchInput,
                onValueChange = { viewModel.contactIntents(ContactIntent.SearchInput(it)) },
                placeholder = {
                    Text(
                        text = "Search",
                        color = Grey,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                },
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Grey, unfocusedTextColor = Grey,
                    focusedContainerColor = DarkGrey,
                    unfocusedContainerColor = DarkGrey,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                ),
                leadingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.search),
                        contentDescription = null,
                        tint = Color.Unspecified,
                        modifier = Modifier
                            .padding(start = 10.dp)
                            .size(25.dp)
                    )
                },
                modifier = Modifier
                    .padding(horizontal = 15.dp, vertical = 30.dp)
                    .fillMaxWidth()
                    .border(
                        width = if (state.searchInput.isNotEmpty()) 2.dp else 0.dp,
                        brush = Btn,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .height(55.dp)
                    .background(color = Color.Transparent, shape = RoundedCornerShape(12.dp)),
                shape = RoundedCornerShape(12.dp)
            )

            Text(
                text = "Contacts",
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                color = Color.White,
                modifier = Modifier.padding(top = 20.dp, start = 15.dp)
            )

           if (state.contacts.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier
                        .padding(horizontal = 15.dp)
                        .padding(top = 20.dp)
                ) {
                    items(contactsList) { contactUi ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { viewModel.contactIntents(ContactIntent.CallContact(contactUi.phone)) }
                        ) {
                            Image(
                                painter = painterResource(R.drawable.phone),
                                contentDescription = null,
                                modifier = Modifier.size(45.dp)
                            )
                            Column(
                                modifier = Modifier
                                    .padding(horizontal = 15.dp)
                                    .fillMaxWidth()
                            ) {
                                Text(
                                    text = contactUi.name,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 21.sp,
                                    color = Color.White,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = contactUi.phone,
                                    fontSize = 16.sp,
                                    color = Grey,
                                    modifier = Modifier.padding(top = 5.dp),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))
                    }
                }
            } else {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = "There is no contact yet",
                        color = Color.White,
                        fontSize = 30.sp,
                        modifier = Modifier.padding(bottom = 70.dp)
                    )
                }
            }
        }
    }
}

