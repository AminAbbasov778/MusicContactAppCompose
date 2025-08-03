package com.example.musiccontactapp.domain.usecases

import com.example.musiccontactapp.presentation.models.ContactUi
import javax.inject.Inject

class SearchContactsUseCase @Inject constructor() {
    operator fun invoke(contactList: List<ContactUi>, input: String): List<ContactUi> {
        val trimmedInput = input.trim()
        if (trimmedInput.isBlank()) return contactList
        return contactList.filter {
            it.name.contains(trimmedInput, true) || it.phone.contains(
                trimmedInput,
                true
            )
        }

    }

}