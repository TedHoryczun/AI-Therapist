package com.devlanding.aitherapist.di

import com.aallam.openai.client.OpenAI
import com.devlanding.aitherapist.data.OpenAIRepository
import com.devlanding.aitherapist.data.ThreadDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    @Singleton
    fun openAiRepository(openAI: OpenAI, threadDao: ThreadDao) =
        OpenAIRepository(openAI, threadDao)
}
