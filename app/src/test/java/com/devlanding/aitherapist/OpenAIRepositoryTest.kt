package com.devlanding.aitherapist

import com.aallam.openai.api.BetaOpenAI
import com.aallam.openai.api.http.Timeout
import com.aallam.openai.client.OpenAI
import com.devlanding.aitherapist.data.Constants
import com.devlanding.aitherapist.data.OpenAIRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.time.Duration.Companion.seconds

class OpenAIRepositoryTest {

    @OptIn(ExperimentalCoroutinesApi::class, BetaOpenAI::class)
    @Test
    fun testGettingMessages() = runTest(UnconfinedTestDispatcher()){
        val openAi = OpenAI(
            token = Constants.openAIApiKey,
            timeout = Timeout(socket = 60.seconds),
        )
        val repo = OpenAIRepository(openAi, MockThreadDao())
        repo.initialize()
        repo.getMessages().collectLatest { msg ->
            println(msg)

        }

    }
}