package com.example.musiccontactapp.presentation.mappers

import com.example.musiccontactapp.domain.models.MusicModel
import com.example.musiccontactapp.presentation.models.MusicUi

fun MusicModel.toUi(): MusicUi {
    return MusicUi(title, artist, data, durationSeconds, imageResId)
}