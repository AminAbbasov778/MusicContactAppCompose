// utils/BatteryStatusReceiver.kt
package com.example.musiccontactapp.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object BatteryLevelObserver {
    private val _batteryLevel = MutableStateFlow(100)
    val batteryLevel: StateFlow<Int> get() = _batteryLevel

    fun register(context: Context) {
        val filter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        context.registerReceiver(object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val level = intent?.getIntExtra("level", -1) ?: -1
                _batteryLevel.value = level
            }
        }, filter)
    }
}
