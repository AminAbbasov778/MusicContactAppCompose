package com.example.musiccontactapp.data.mappers

import com.example.musiccontactapp.data.models.Contact
import com.example.musiccontactapp.domain.models.ContactModel

fun Contact.toDomain(): ContactModel = ContactModel(name = name, phone = phone)
