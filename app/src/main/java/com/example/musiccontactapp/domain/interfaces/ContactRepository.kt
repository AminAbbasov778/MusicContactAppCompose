package com.example.musiccontactapp.domain.interfaces

import com.example.musiccontactapp.domain.models.ContactModel

interface ContactRepository {
    fun getContacts(): Result<List<ContactModel>>
}