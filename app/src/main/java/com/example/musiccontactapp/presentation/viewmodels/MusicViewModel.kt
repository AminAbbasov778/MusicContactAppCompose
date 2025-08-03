package com.example.musiccontactapp.presentation.viewmodels

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musiccontactapp.domain.usecases.GetMusicListUseCase
import com.example.musiccontactapp.domain.usecases.SearchMusicsListUseCase
import com.example.musiccontactapp.presentation.intents.MusicIntent
import com.example.musiccontactapp.presentation.mappers.toUi
import com.example.musiccontactapp.presentation.models.MusicUi
import com.example.musiccontactapp.presentation.states.MusicState
import com.example.musiccontactapp.service.MusicPlayerService
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MusicViewModel @Inject constructor(
    @ApplicationContext val context: Context,
    private val getMusicListUseCase: GetMusicListUseCase,
    private val searchMusicsListUseCase: SearchMusicsListUseCase
) : ViewModel() {

    private val _musicState = MutableStateFlow(MusicState())
    val musicState: StateFlow<MusicState> get() = _musicState

    private var musicService: MusicPlayerService? = null
    private var updateJob: Job? = null
    private var isServiceBound = false

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, binder: IBinder?) {
            musicService = (binder as? MusicPlayerService.MusicBinder)?.getService()
            restorePlaybackState()
            startUpdatingPosition()
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            musicService = null
            updateJob?.cancel()
        }
    }


    fun syncMusicStateFromService(service: MusicPlayerService) {
        val position = service.getCurrentPosition()
        val isPaused = service.isPaused
        val path = service.currentPath

        _musicState.update {
            it.copy(
                savedPosition = position,
                savedIsPaused = isPaused,
                currentMusicPath = path,
                isMusicAlreadyStarted = true
            )
        }
    }


    init {
        bindToMusicService()
    }

    private fun bindToMusicService() {
        val intent = Intent(context, MusicPlayerService::class.java)
        isServiceBound = context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
    }

    private fun unbindService() {
        if (isServiceBound) {
            context.unbindService(serviceConnection)
            isServiceBound = false
        }
    }

    fun musicIntents(intent: MusicIntent) {
        when (intent) {
            is MusicIntent.GetMusics -> getMusics()
            is MusicIntent.SearchInput -> searchInput(intent.input)
            is MusicIntent.PlayMusic -> playMusic(intent.music)
            is MusicIntent.OnCLickMusic -> setCurrentMusic(intent.music)
            is MusicIntent.ClearCurrentMusic -> clearCurrentMusic()
            is MusicIntent.PauseOrResume -> pauseOrResume()
            is MusicIntent.SeekTo -> seekTo(intent.millis)
            is MusicIntent.SetCurrentMusic -> setCurrentMusic(intent.music)
            is MusicIntent.FastSeekForward -> fastSeekForward()
            is MusicIntent.FastSeekBackward -> fastSeekBackward()
            is MusicIntent.SkipToNextMusic -> skipToNextMusic(intent.currentMusic)
            is MusicIntent.SkipToPreviousMusic -> skipToPreviousMusic(intent.currentMusic)
        }
    }

    private fun restorePlaybackState() {
        val state = _musicState.value
        if (state.currentMusicPath != null && state.currentMusic != null) {
            if (musicService?.currentPath == state.currentMusicPath) {
                _musicState.value = state.copy(
                    currentPosition = musicService?.getCurrentPosition() ?: state.savedPosition,
                    isPaused = musicService?.isPaused ?: state.savedIsPaused,
                    savedPosition = musicService?.getCurrentPosition() ?: state.savedPosition,
                    savedIsPaused = musicService?.isPaused ?: state.savedIsPaused
                )
            } else {
                musicService?.play(state.currentMusicPath)
                musicService?.seekTo(state.savedPosition)
                if (state.savedIsPaused) {
                    musicService?.pause()
                } else {
                    startUpdatingPosition()
                }
                _musicState.value = state.copy(
                    currentPosition = state.savedPosition,
                    isPaused = state.savedIsPaused
                )
            }
        }
    }

    private fun playMusic(currentMusic: MusicUi?) {
        currentMusic?.let {
            val intent = Intent(context, MusicPlayerService::class.java).apply {
                action = MusicPlayerService.ACTION_PLAY_PATH
                putExtra(MusicPlayerService.EXTRA_PATH, it.data)
            }
            ContextCompat.startForegroundService(context, intent)
            musicService?.play(it.data)
            _musicState.value = _musicState.value.copy(
                currentMusic = it,
                currentMusicPath = it.data,
                currentPosition = 0,
                savedPosition = 0,
                isPaused = false,
                savedIsPaused = false
            )
        }
    }

    fun clearCurrentMusic() {
        _musicState.value = _musicState.value.copy(
            currentMusic = null,
            currentMusicPath = null,
            currentPosition = 0,
            savedPosition = 0,
            isPaused = false,
            savedIsPaused = false
        )
    }

    private fun pauseOrResume() {
        val isPaused = _musicState.value.isPaused
        if (isPaused) {
            musicService?.resume()
            startUpdatingPosition()
        } else {
            musicService?.pause()
            updateJob?.cancel()
        }
        _musicState.value = _musicState.value.copy(
            isPaused = !isPaused,
            savedIsPaused = !isPaused,
            savedPosition = musicService?.getCurrentPosition() ?: _musicState.value.currentPosition
        )
    }

    private fun seekTo(millis: Int) {
        musicService?.seekTo(millis)
        _musicState.value = _musicState.value.copy(
            currentPosition = millis,
            savedPosition = millis
        )
    }

    private fun startUpdatingPosition() {
        updateJob?.cancel()
        updateJob = viewModelScope.launch {
            while (true) {
                val position = musicService?.getCurrentPosition() ?: 0
                _musicState.value = _musicState.value.copy(
                    currentPosition = position,
                    savedPosition = position
                )
                delay(1000)
            }
        }
    }

    private fun getMusics() {
        _musicState.value = _musicState.value.copy(isLoading = true)
        viewModelScope.launch {
            val result = getMusicListUseCase()
            _musicState.value = if (result.isSuccess) {
                val musics = result.getOrNull()?.map { it.toUi() } ?: emptyList()
                val currentMusic = musics.find { it.data == _musicState.value.currentMusicPath }
                _musicState.value.copy(
                    musics = musics,
                    currentMusic = currentMusic ?: _musicState.value.currentMusic,
                    isLoading = false
                )
            } else {
                _musicState.value.copy(error = "Failed to get musics")
            }
        }
    }

    private fun setCurrentMusic(music: MusicUi) {
        _musicState.value = _musicState.value.copy(
            currentMusic = music,
            currentMusicPath = music.data,
            currentPosition = 0,
            savedPosition = 0,
            isPaused = false,
            savedIsPaused = false
        )
    }

    private fun searchInput(input: String) {
        _musicState.value = _musicState.value.copy(searchInput = input)
        val result = searchMusicsListUseCase(_musicState.value.musics, input)
        _musicState.value = _musicState.value.copy(searchResult = result)
    }

    override fun onCleared() {
        super.onCleared()
        unbindService()
        updateJob?.cancel()
    }

    private fun fastSeekForward() {
        musicService?.let {
            val newPosition = (it.getCurrentPosition() + 5000).coerceAtMost(it.getDuration())
            it.seekTo(newPosition)
            _musicState.value = _musicState.value.copy(
                currentPosition = newPosition,
                savedPosition = newPosition
            )
        }
    }

    private fun fastSeekBackward() {
        musicService?.let {
            val newPosition = (it.getCurrentPosition() - 5000).coerceAtLeast(0)
            it.seekTo(newPosition)
            _musicState.value = _musicState.value.copy(
                currentPosition = newPosition,
                savedPosition = newPosition
            )
        }
    }

    private fun skipToNextMusic(currentMusic: MusicUi?) {
        currentMusic?.let {
            val currentIndex = _musicState.value.musics.indexOf(it)
            playMusicByIndex(currentIndex + 1)
        }
    }

    private fun skipToPreviousMusic(currentMusic: MusicUi?) {
        currentMusic?.let {
            val currentIndex = _musicState.value.musics.indexOf(it)
            playMusicByIndex(currentIndex - 1)
        }
    }

    private fun playMusicByIndex(index: Int) {
        val musics = _musicState.value.musics
        if (index in musics.indices) {
            val selectedMusic = musics[index]
            _musicState.value = _musicState.value.copy(
                currentMusic = selectedMusic,
                currentMusicPath = selectedMusic.data,
                currentPosition = 0,
                savedPosition = 0,
                isPaused = false,
                savedIsPaused = false
            )
            playMusic(selectedMusic)
        }
    }
}