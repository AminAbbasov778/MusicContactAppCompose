package com.example.musiccontactapp.di

import android.content.Context
import com.example.musiccontactapp.data.repositories.ContactRepositoryImpl
import com.example.musiccontactapp.data.repositories.MusicRepositoryImpl
import com.example.musiccontactapp.domain.interfaces.ContactRepository
import com.example.musiccontactapp.domain.interfaces.MusicRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideMusicRepository(@ApplicationContext context: Context): MusicRepository {
        return MusicRepositoryImpl(context)
    }

    @Provides
    @Singleton
    fun provideContactRepository(@ApplicationContext context: Context): ContactRepository =
        ContactRepositoryImpl(context)

}