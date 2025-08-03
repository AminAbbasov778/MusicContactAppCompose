package com.example.musiccontactapp.presentation.screens

import android.annotation.SuppressLint
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.musiccontactapp.presentation.activities.ChoiceActivity
import com.example.musiccontactapp.presentation.intents.MainIntent
import com.example.musiccontactapp.presentation.viewmodels.MainViewModel
import com.example.musiccontactapp.ui.theme.Btn
import com.example.musiccontactapp.ui.theme.DarkGrey
import com.example.musiccontactapp.ui.theme.DisableBtn
import com.example.musiccontactapp.ui.theme.Grey

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun UserNameScreen(paddingValues: PaddingValues,mainViewModel: MainViewModel = hiltViewModel()) {
    val state by mainViewModel.state.collectAsState()
    val context = LocalContext.current

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
                )
        ) {
            Text(
                text = "Welcome Music&ContactApp!",
                lineHeight = 20.sp,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                color = Color.White,
                modifier = Modifier.padding(top = 100.dp, start = 15.dp)
            )
            Text(
                text = "Please, Enter Your Username to access",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Grey,
                modifier = Modifier.padding(top = 10.dp, start = 15.dp)
            )

            TextField(
                value = state.username,
                onValueChange = { mainViewModel.mainIntents(MainIntent.SearchInput(it))  },
                label = null,
                placeholder = {
                    Text(
                        text = "Username",
                        color = Grey,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium, modifier = Modifier.align(Alignment.CenterHorizontally),textAlign = TextAlign.Center
                    )
                },
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Grey,
                    unfocusedTextColor = Grey,
                    focusedContainerColor = DarkGrey,
                    unfocusedContainerColor = DarkGrey,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                ),
                modifier = Modifier
                    .padding(horizontal = 15.dp)
                    .padding(top = 30.dp)
                    .fillMaxWidth()
                    .then(
                        if (state.username.isNotEmpty()) Modifier.border(
                            width = 2.dp,
                            brush = Btn,
                            shape = RoundedCornerShape(12.dp)
                        ) else Modifier
                    )
                    .height(55.dp)
                    .background(color = Color.Transparent, shape = RoundedCornerShape(12.dp)),
                shape = RoundedCornerShape(12.dp),
            )

            Button(
                onClick = {
                    if (state.username.trim().isNotEmpty()) {
                        val intent = Intent(context, ChoiceActivity::class.java)
                        intent.putExtra("username",state.username)
                        context.startActivity(intent)
                    }
                },
                enabled = state.username.isNotEmpty(),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                modifier = Modifier
                    .padding(horizontal = 15.dp, vertical = 20.dp)
                    .height(50.dp)
                    .fillMaxWidth()
                    .background(
                        brush = if (state.username.isNotEmpty()) Btn else DisableBtn,
                        shape = RoundedCornerShape(12.dp)
                    )
            ) {
                Text(
                    text = "Next",
                    color = if (state.username.isNotEmpty()) Color.White else Grey,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}
