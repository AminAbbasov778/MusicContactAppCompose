package com.example.musiccontactapp.presentation.intents

import com.example.musiccontactapp.presentation.models.MusicUi

sealed class MusicIntent {
    object GetMusics : MusicIntent()
    data class SearchInput(val input : String) : MusicIntent()
    data class PlayMusic(val music : MusicUi) :MusicIntent()
    data class OnCLickMusic(val music : MusicUi) : MusicIntent()
    object  ClearCurrentMusic : MusicIntent()
    object PauseOrResume : MusicIntent()
    data class SeekTo(val  millis: Int) : MusicIntent()
    data class  SetCurrentMusic(val music: MusicUi) : MusicIntent()
    data class  SkipToNextMusic(val currentMusic: MusicUi?) : MusicIntent()
    data class SkipToPreviousMusic(val currentMusic: MusicUi?) : MusicIntent()
    object  FastSeekForward : MusicIntent()
    object  FastSeekBackward : MusicIntent()

}