package com.example.musiccontactapp.presentation.viewmodels

import androidx.lifecycle.ViewModel
import com.example.musiccontactapp.presentation.intents.MainIntent
import com.example.musiccontactapp.presentation.states.MainState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {

    private  val _state = MutableStateFlow(MainState())
    val state : StateFlow<MainState> get() = _state


    fun mainIntents(mainIntent: MainIntent){
        when(mainIntent){
            is MainIntent.SearchInput ->searchInput(mainIntent.username)
        }
    }

    private fun searchInput(username : String){
        _state.value = _state.value.copy(username = username)
    }
}