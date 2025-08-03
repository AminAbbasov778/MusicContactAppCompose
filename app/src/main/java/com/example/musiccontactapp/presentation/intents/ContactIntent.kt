package com.example.musiccontactapp.presentation.intents

sealed class ContactIntent {
     object GetContacts: ContactIntent()
     data class SearchInput(val searchInput: String) : ContactIntent()
     data class CallContact(val phone : String) : ContactIntent()
}