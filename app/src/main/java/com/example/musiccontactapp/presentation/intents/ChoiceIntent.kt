package com.example.musiccontactapp.presentation.intents

sealed class ChoiceIntent {
    data class OnIconBtnClicked (val value : Boolean = false) : ChoiceIntent()
}