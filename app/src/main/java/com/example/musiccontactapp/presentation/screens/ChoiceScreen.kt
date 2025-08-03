package com.example.musiccontactapp.presentation.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.musiccontactapp.R
import com.example.musiccontactapp.presentation.intents.ChoiceIntent
import com.example.musiccontactapp.presentation.viewmodels.ChoiceViewModel
import com.example.musiccontactapp.ui.theme.Btn

@SuppressLint("UnrememberedMutableState", "UnusedBoxWithConstraintsScope")
@Composable
fun ChoiceScreen(paddingValues: PaddingValues,username: String,onMusicsClick: () -> Unit, onContactsClick: () -> Unit,viewModel : ChoiceViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.choiceIntents(ChoiceIntent.OnIconBtnClicked(false))

    }

    BoxWithConstraints(
        modifier = Modifier
            .padding(paddingValues)
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

                ), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = "Welcome ${username}",
                lineHeight = 20.sp,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                color = Color.White,
                modifier = Modifier.padding(top = 100.dp, start = 15.dp)
            )
            Spacer(Modifier.height(20.dp))

            IconButton(
                onClick = { if(!state.isIconBtClicked) {
                    onMusicsClick()
                    viewModel.choiceIntents(ChoiceIntent.OnIconBtnClicked(true))
                }},
                modifier = Modifier
                    .padding(horizontal = 15.dp, vertical = 10.dp)
                    .height(50.dp)
                    .fillMaxWidth()
                    .background(
                        brush = Btn,
                        shape = RoundedCornerShape(12.dp)
                    )
            ) {
                Icon(
                    painter = painterResource(R.drawable.music),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
            }

            IconButton(
                onClick = { if (!state.isIconBtClicked) {
                    onContactsClick()
                    viewModel.choiceIntents(ChoiceIntent.OnIconBtnClicked(true))

                } },
                modifier = Modifier
                    .padding(horizontal = 15.dp, vertical = 10.dp)
                    .height(50.dp)
                    .fillMaxWidth()
                    .background(
                        brush = Btn,
                        shape = RoundedCornerShape(12.dp)
                    )
            ) {
                Icon(
                    painter = painterResource(R.drawable.phone),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
            }

        }
    }

}
