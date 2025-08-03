package com.example.musiccontactapp.presentation.states

import com.example.musiccontactapp.presentation.models.MusicUi

data class MusicState(
    val musics: List<MusicUi> = emptyList(),
    val searchInput: String = "",
    val searchResult: List<MusicUi> = emptyList(),
    val currentMusic: MusicUi? = null,
    val currentPosition: Int = 0,
    val isPaused: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null,
    val currentMusicPath: String? = null,
    val savedPosition: Int = 0,
    val savedIsPaused: Boolean = false,
    val isMusicAlreadyStarted: Boolean = false
)
