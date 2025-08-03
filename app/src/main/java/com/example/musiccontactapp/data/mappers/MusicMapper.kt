package com.example.musiccontactapp.data.mappers

import com.example.musiccontactapp.data.models.Music
import com.example.musiccontactapp.domain.models.MusicModel

fun Music.toDomain(): MusicModel{
    return MusicModel(title,artist,data,durationSeconds,imageResId)
}