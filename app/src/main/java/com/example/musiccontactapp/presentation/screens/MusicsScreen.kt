package com.example.musiccontactapp.presentation.screens

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.musiccontactapp.R
import com.example.musiccontactapp.presentation.intents.MusicIntent
import com.example.musiccontactapp.presentation.viewmodels.MusicViewModel
import com.example.musiccontactapp.ui.theme.Btn
import com.example.musiccontactapp.ui.theme.DarkGrey
import com.example.musiccontactapp.ui.theme.Grey
import com.example.musiccontactapp.ui.theme.MusicContactAppTheme

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun MusicsScreen(viewModel: MusicViewModel, onMusicsClick : (Unit) -> Unit){


    val context = LocalContext.current
    val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
        Manifest.permission.READ_MEDIA_AUDIO
    else
        Manifest.permission.READ_EXTERNAL_STORAGE

    val activity = context as? Activity
    val state by  viewModel.musicState.collectAsState()

    val musicList = if(state.searchInput.isBlank()) state.musics else state.searchResult

    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) {

    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
            if (granted) {
                viewModel.musicIntents(MusicIntent.GetMusics)
            }
        }
    )



    LaunchedEffect(Unit) {
        if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
            permissionLauncher.launch(permission)
        } else {
            viewModel.musicIntents(MusicIntent.GetMusics)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    LaunchedEffect(Unit){
        viewModel.musicIntents(MusicIntent.GetMusics)
    }


    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
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
                lineHeight = 20.sp,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                color = Color.White,
                modifier = Modifier.padding(top = 100.dp, start = 15.dp)
            )

            Text(
                text = "What do you feel like today?",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Grey,
                modifier = Modifier.padding(top = 10.dp, start = 15.dp)
            )



            TextField(
                value = state.searchInput,
                onValueChange = { viewModel.musicIntents(MusicIntent.SearchInput(it)) },
                label = null,
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
                    .padding(horizontal = 15.dp)
                    .padding(top = 30.dp)
                    .fillMaxWidth()
                    .then(
                        if (state.searchInput.isNotEmpty()) Modifier.border(
                            width = 2.dp,
                            brush = Btn,
                            shape = RoundedCornerShape(12.dp)
                        ) else Modifier

                    )
                    .height(55.dp)
                    .background(color = Color.Transparent, shape = RoundedCornerShape(12.dp)),
                shape = RoundedCornerShape(12.dp),

                )

            Text(
                text = "Musics",
                lineHeight = 20.sp,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                color = Color.White,
                modifier = Modifier.padding(top = 20.dp, start = 15.dp)
            )
            if(state.musics.isNotEmpty()){
                LazyColumn(modifier = Modifier.padding(horizontal = 15.dp).padding(top = 20.dp)){
                    items(musicList){
                        Row(modifier = Modifier.fillMaxWidth().clickable{
                            viewModel.musicIntents(MusicIntent.OnCLickMusic(it))
                            onMusicsClick(Unit)
                        }){
                            Image(
                                painter = painterResource(it.imageResId),
                                contentDescription = null,
                                modifier = Modifier.size(45.dp))

                            Column(modifier = Modifier.padding(horizontal = 15.dp).fillMaxWidth()){
                                Text(
                                    text = it.title,
                                    lineHeight = 19.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 21.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    color = Color.White,
                                )
                                Text(
                                    text = it.artist,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    color = Grey,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier.padding(top = 5.dp)
                                )

                            }



                        }

                        Spacer(modifier = Modifier.height(20.dp))
                    }
                }

            }else{
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                    Text(text = "There is no music yet", color = Color.White, fontSize = 30.sp, modifier = Modifier.padding(bottom = 70.dp))

                }
            }


        }
    }
}

@Preview(showBackground = true)
@Composable
fun MusicsScreenPreview() {
    MusicContactAppTheme {

    }
}

