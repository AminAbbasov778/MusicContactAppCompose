package com.example.musiccontactapp.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musiccontactapp.domain.usecases.GetContactsUseCase
import com.example.musiccontactapp.domain.usecases.SearchContactsUseCase
import com.example.musiccontactapp.presentation.intents.ContactIntent
import com.example.musiccontactapp.presentation.states.ContactState
import com.example.musiccontactapp.presentation.mappers.toUi
import com.example.musiccontactapp.presentation.states.ContactEffect
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContactViewModel @Inject constructor(private val getContactsUseCase: GetContactsUseCase,private val searchContactsUseCase: SearchContactsUseCase) :
    ViewModel() {

    private val _state = MutableStateFlow(ContactState())
    val state: StateFlow<ContactState> = _state.asStateFlow()

    private val _effect = MutableStateFlow(ContactEffect())
    val effect : StateFlow<ContactEffect> get() = _effect

    fun contactIntents (intent: ContactIntent) {
        when (intent) {
            is ContactIntent.GetContacts -> getContacts()
            is ContactIntent.SearchInput -> searchInput(intent.searchInput)
            is ContactIntent.CallContact ->callContact(intent.phone)

        }
    }

   private fun getContacts() {
        _state.value = _state.value.copy(loading = true)
        viewModelScope.launch {
          val result =  getContactsUseCase()
            _state.value =  if(result.isSuccess) ContactState(contacts =result.getOrNull()?.map { it.toUi() } ?: emptyList(), loading = false)
                else ContactState(error = "Failed to get Contacts", loading = false)

        }
    }


    private fun searchInput(input : String){
        _state.value = _state.value.copy(searchInput= input)
        val searchResult = searchContactsUseCase(_state.value.contacts,input)
       _state.value = _state.value.copy(searchResult =  searchResult)
    }

    private fun callContact(phone : String){
        _effect.value = _effect.value.copy(openDialer = phone)
    }

}