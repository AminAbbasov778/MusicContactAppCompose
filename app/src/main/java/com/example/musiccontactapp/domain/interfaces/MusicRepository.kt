package com.example.musiccontactapp.domain.interfaces

import com.example.musiccontactapp.domain.models.MusicModel

interface MusicRepository {
    suspend fun getAllMusics(): Result<List<MusicModel>>
}