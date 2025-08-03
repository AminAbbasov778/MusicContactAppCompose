package com.example.musiccontactapp.presentation.screens

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.musiccontactapp.R
import com.example.musiccontactapp.presentation.intents.MusicIntent
import com.example.musiccontactapp.presentation.utils.formatMillisToTime
import com.example.musiccontactapp.presentation.viewmodels.MusicViewModel
import com.example.musiccontactapp.service.MusicPlayerService
import com.example.musiccontactapp.ui.theme.Btn
import com.example.musiccontactapp.ui.theme.Grey
import com.example.musiccontactapp.utils.BatteryLevelObserver
import kotlinx.coroutines.delay

@Composable
fun PlayScreen(viewModel: MusicViewModel, onBackClick: (Unit) -> Unit) {
    val state by viewModel.musicState.collectAsState()

    var isUserInteracting by remember { mutableStateOf(false) }
    var sliderPosition by remember { mutableStateOf(state.savedPosition.toFloat()) }

    val isLongPressedBackward = remember { mutableStateOf(false) }
    val isLongPressedForward = remember { mutableStateOf(false) }

    val batteryLevel by BatteryLevelObserver.batteryLevel.collectAsState(initial = 100)
    val showAnimation = batteryLevel > 20

    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.musicbg))

    val progress by animateLottieCompositionAsState(
        composition, iterations = LottieConstants.IterateForever,
        isPlaying = showAnimation,
    )

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        val serviceIntent = Intent(context, MusicPlayerService::class.java)
        context.bindService(serviceIntent, object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName?, binder: IBinder?) {
                val musicService = (binder as MusicPlayerService.MusicBinder).getService()
                viewModel.syncMusicStateFromService(musicService)
            }

            override fun onServiceDisconnected(name: ComponentName?) {}
        }, Context.BIND_AUTO_CREATE)
    }


    LaunchedEffect(state.currentPosition) {
        if (!isUserInteracting) {
            sliderPosition = state.currentPosition.toFloat()
        }
    }

    LaunchedEffect(Unit) {
        if (!state.isMusicAlreadyStarted &&
            state.currentMusic != null &&
            state.currentMusicPath != null
        ) {
            viewModel.musicIntents(MusicIntent.PlayMusic(state.currentMusic!!))
        }
    }


    LaunchedEffect(isLongPressedBackward.value) {
        while (isLongPressedBackward.value) {
            delay(300)
            viewModel.musicIntents(MusicIntent.FastSeekBackward)
        }
    }
    LaunchedEffect(isLongPressedForward.value) {
        while (isLongPressedForward.value) {
            delay(300)
            viewModel.musicIntents(MusicIntent.FastSeekForward)
        }
    }

    val durationMillis = (state.currentMusic?.durationSeconds ?: 0) * 1000L

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF781ECF), Color(0xFF3B1D5E), Color.Black)
                )
            )
            .padding(23.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .padding(top = 60.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                painter = painterResource(R.drawable.back),
                contentDescription = "back",
                tint = Color.White,
                modifier = Modifier
                    .size(30.dp)
                    .clickable {
                        onBackClick(Unit)
                    }
            )
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "back",
                tint = Color.White,
                modifier = Modifier
                    .size(30.dp)
                    .rotate(90f)
            )
        }

        Image(
            painter = painterResource(id = R.drawable.playimg1),
            contentDescription = "Cover Image",
            modifier = Modifier
                .padding(top = 40.dp, start = 20.dp, end = 20.dp)
                .fillMaxWidth()
                .height(350.dp)
                .clip(RoundedCornerShape(16.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = state.currentMusic?.title ?: "",
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = Color.White
        )

        Text(
            text = state.currentMusic?.artist ?: "",
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp,
            color = Grey,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(top = 4.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(Btn)
        ) {
            Slider(
                value = sliderPosition,
                onValueChange = {
                    isUserInteracting = true
                    sliderPosition = it
                },
                onValueChangeFinished = {
                    viewModel.musicIntents(MusicIntent.SeekTo(sliderPosition.toInt()))
                    isUserInteracting = false
                },
                valueRange = 0f..durationMillis.toFloat().coerceAtLeast(1f),
                modifier = Modifier.fillMaxWidth(),
                colors = SliderDefaults.colors(
                    thumbColor = Color.White,
                    activeTrackColor = Color.White,
                    inactiveTrackColor = Color.Gray.copy(alpha = 0.5f)
                )
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = formatMillisToTime(sliderPosition.toLong()),
                color = Color.White,
                modifier = Modifier.padding(top = 5.dp),
                fontSize = 14.sp
            )
            Text(
                text = formatMillisToTime(durationMillis),
                modifier = Modifier.padding(top = 5.dp),
                color = Color.White,
                fontSize = 14.sp
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                painter = painterResource(R.drawable.skip),
                contentDescription = "Previous",
                modifier = Modifier
                    .size(40.dp)
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onTap = { viewModel.musicIntents(MusicIntent.SkipToPreviousMusic(state.currentMusic)) },
                            onLongPress = { isLongPressedBackward.value = true },
                            onPress = {
                                tryAwaitRelease()
                                isLongPressedBackward.value = false
                            }
                        )
                    },
                tint = Color.Unspecified
            )

            Spacer(modifier = Modifier.width(40.dp))

            Box(modifier = Modifier.wrapContentSize(), contentAlignment = Alignment.Center) {
                if (showAnimation) {
                    LottieAnimation(
                        composition,
                        progress,
                        modifier = Modifier
                            .width(150.dp)
                            .height(150.dp)
                            .alpha(0.3f)
                    )
                }

                Box(
                    modifier = Modifier
                        .size(90.dp)
                        .clip(RoundedCornerShape(45.dp))
                        .background(Btn)
                        .clickable { viewModel.musicIntents(MusicIntent.PauseOrResume) },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(if (state.isPaused) R.drawable.play else R.drawable.pause),
                        contentDescription = if (state.isPaused) "Play" else "Pause",
                        tint = Color.Unspecified,
                        modifier = Modifier.size(40.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(40.dp))

            Icon(
                painter = painterResource(R.drawable.skip),
                contentDescription = "Next",
                tint = Color.Unspecified,
                modifier = Modifier
                    .size(40.dp)
                    .rotate(180f)
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onTap = { viewModel.musicIntents(MusicIntent.SkipToNextMusic(state.currentMusic)) },
                            onLongPress = { isLongPressedForward.value = true },
                            onPress = {
                                tryAwaitRelease()
                                isLongPressedForward.value = false
                            }
                        )
                    }
            )
        }
    }
}