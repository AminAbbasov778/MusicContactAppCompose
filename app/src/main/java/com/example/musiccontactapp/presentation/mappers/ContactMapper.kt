package com.example.musiccontactapp.presentation.mappers

import com.example.musiccontactapp.domain.models.ContactModel
import com.example.musiccontactapp.presentation.models.ContactUi

fun ContactModel.toUi()= ContactUi(name, phone)