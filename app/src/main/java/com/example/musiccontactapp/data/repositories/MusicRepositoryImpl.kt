package com.example.musiccontactapp.data.repositories

import android.content.Context
import android.provider.MediaStore
import com.example.musiccontactapp.R
import com.example.musiccontactapp.data.mappers.toDomain
import com.example.musiccontactapp.data.models.Music
import com.example.musiccontactapp.domain.interfaces.MusicRepository
import com.example.musiccontactapp.domain.models.MusicModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class MusicRepositoryImpl @Inject constructor(@ApplicationContext private val context: Context) :
    MusicRepository {
    override suspend fun getAllMusics(): Result<List<MusicModel>> {
        val defaultImages = listOf(R.drawable.musicimg1, R.drawable.musicimg2, R.drawable.musicimg3)
        val musicList = mutableListOf<MusicModel>()
        try {
            val contentResolver = context.contentResolver
            val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
            val selection = "${MediaStore.Audio.Media.IS_MUSIC} != 0"
            val projection = arrayOf(
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DURATION
            )
            var index = 0
            val cursor = contentResolver.query(uri, projection, selection, null, null)
          return  cursor?.use {
                val titleIndex = it.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
                val artistsIndex = it.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
                val dataIndex = it.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
                val durationIndex = it.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)

                while (it.moveToNext()) {
                    val title = it.getString(titleIndex)
                    val artist = it.getString(artistsIndex)
                    val data = it.getString(dataIndex)
                    val duration = it.getLong(durationIndex)
                    val imageResId = defaultImages[index % defaultImages.size]
                    index++

                    val durationSeconds = duration / 1000

                    musicList.add(
                        Music(
                            title,
                            artist,
                            data,
                            durationSeconds,
                            imageResId
                        ).toDomain()
                    )

                }
              Result.success(musicList)
            } ?: Result.failure(Exception("null"))


        }catch (e: Exception){
         return   Result.failure(e)
        }




    }
}