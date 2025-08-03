package com.example.musiccontactapp.domain.models

data class MusicModel(
    val title: String,
    val artist: String,
    val data: String,val durationSeconds: Long, val imageResId: Int
)