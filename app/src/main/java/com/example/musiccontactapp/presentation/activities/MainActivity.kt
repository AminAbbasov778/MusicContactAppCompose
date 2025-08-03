package com.example.musiccontactapp.presentation.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.musiccontactapp.R
import com.example.musiccontactapp.presentation.viewmodels.MainViewModel
import com.example.musiccontactapp.service.MusicPlayerService
import com.example.musiccontactapp.ui.theme.*
import com.example.musiccontactapp.utils.BatteryLevelObserver
import dagger.hilt.android.AndroidEntryPoint
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import com.example.musiccontactapp.presentation.intents.MainIntent
import com.example.musiccontactapp.presentation.screens.UserNameScreen


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        BatteryLevelObserver.register(this)
        setContent {
            MusicContactAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    UserNameScreen(innerPadding)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopService(Intent(this, MusicPlayerService::class.java))
    }
}



