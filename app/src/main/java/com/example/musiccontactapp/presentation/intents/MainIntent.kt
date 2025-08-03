package com.example.musiccontactapp.presentation.intents

sealed class MainIntent {
    data class SearchInput(val username : String) : MainIntent()
}