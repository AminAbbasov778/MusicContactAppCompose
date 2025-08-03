package com.example.musiccontactapp.domain.usecases

import com.example.musiccontactapp.presentation.models.MusicUi
import javax.inject.Inject

class SearchMusicsListUseCase @Inject constructor() {
    operator fun invoke( musics : List<MusicUi>, input : String): List<MusicUi>{
        val trimmedInput = input.trim()
        if(trimmedInput.isBlank()) return musics
        return musics.filter { it.title.contains(trimmedInput,  true) ||  it.artist.contains(trimmedInput,true)}
    }
}