package com.example.musiccontactapp.presentation.states

import com.example.musiccontactapp.presentation.models.ContactUi

data class ContactState(
    val contacts: List<ContactUi> = emptyList(),
    val searchInput: String = "",
    val loading: Boolean = false,
    val error: String = "",
    val searchResult : List<ContactUi> = emptyList()
) {

}