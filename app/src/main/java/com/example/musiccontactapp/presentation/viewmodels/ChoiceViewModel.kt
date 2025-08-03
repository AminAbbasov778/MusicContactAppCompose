package com.example.musiccontactapp.presentation.viewmodels

import androidx.lifecycle.ViewModel
import com.example.musiccontactapp.presentation.intents.ChoiceIntent
import com.example.musiccontactapp.presentation.states.ChoiceState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ChoiceViewModel @Inject constructor() : ViewModel() {
    private val _state = MutableStateFlow(ChoiceState())
    val state : StateFlow<ChoiceState> get() = _state

    fun choiceIntents(intent: ChoiceIntent){
        when(intent){
            is ChoiceIntent.OnIconBtnClicked -> onIconBtnClicked(intent.value)
        }
    }

    private fun onIconBtnClicked(value : Boolean){
        _state.value = _state.value.copy(isIconBtClicked =value )
    }




}