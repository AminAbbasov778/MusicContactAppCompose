package com.example.musiccontactapp.domain.usecases

import com.example.musiccontactapp.domain.interfaces.ContactRepository
import javax.inject.Inject

class GetContactsUseCase @Inject constructor(private val contactRepository: ContactRepository) {
    operator fun invoke() = contactRepository.getContacts()
}