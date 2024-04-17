package com.devlanding.aitherapist.di

import com.aallam.openai.api.http.Timeout
import com.aallam.openai.client.OpenAI
import com.devlanding.aitherapist.data.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import kotlin.time.Duration.Companion.seconds

@Module
@InstallIn(SingletonComponent::class)
class OpenAIModule {
    @Provides
    @Singleton
    fun openAi() = OpenAI(
        token = Constants.openAIApiKey,
        timeout = Timeout(socket = 60.seconds)
    )



}

