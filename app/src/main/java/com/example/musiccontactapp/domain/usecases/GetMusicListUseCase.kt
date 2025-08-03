package com.example.musiccontactapp.domain.usecases

import com.example.musiccontactapp.domain.interfaces.MusicRepository
import com.example.musiccontactapp.domain.models.MusicModel
import jakarta.inject.Inject

class GetMusicListUseCase @Inject constructor (val musicRepository: MusicRepository) {
    suspend operator fun invoke() = musicRepository.getAllMusics()
}