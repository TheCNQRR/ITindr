package com.example.chat.di

import com.example.chat.data.ChatsRepositoryImpl
import com.example.chat.domain.repository.ChatsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindChatsRepository(impl: ChatsRepositoryImpl): ChatsRepository
}
