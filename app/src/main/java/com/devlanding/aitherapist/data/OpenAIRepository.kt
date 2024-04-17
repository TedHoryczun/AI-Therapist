package com.devlanding.aitherapist.data

import com.aallam.openai.api.BetaOpenAI
import com.aallam.openai.api.assistant.AssistantId
import com.aallam.openai.api.core.Role
import com.aallam.openai.api.core.Status
import com.aallam.openai.api.message.MessageRequest
import com.aallam.openai.api.run.RunRequest
import com.aallam.openai.api.thread.Thread
import com.aallam.openai.api.thread.ThreadId
import com.aallam.openai.client.OpenAI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class OpenAIRepository @Inject constructor(
    private val openAI: OpenAI,
    private val threadDao: ThreadDao
) {
    private var threadId: String? = null
    val state = MutableSharedFlow<OpenAiState>()
    suspend fun initialize() {
        withContext(Dispatchers.IO) {
            val thread = threadDao.getThread()
            if (thread.isNotEmpty()) {
                threadId = thread.first().threadId
                println("theadID: $threadId")
            }
        }
    }

    @OptIn(BetaOpenAI::class)
    suspend fun getMessages() = callbackFlow<List<TherapistMessage>> {
        val thread = if (threadId != null) {
            openAI.thread(ThreadId(threadId!!))
        } else {
            val thread = openAI.thread()
            launch(Dispatchers.IO) {
                threadDao.insertTheread(OpenAIThread(threadId = thread.id.id))
            }
            thread
        } as Thread
        val messages = openAI.messages(thread.id)
        val therapistMessages = messages.mapNotNull { it.toMessageContent() }
        trySend(therapistMessages)
        cancel()
    }

    @OptIn(BetaOpenAI::class)
    fun virtualAssistant(content: String) = callbackFlow<List<TherapistMessage>> {
        val thread = if (threadId != null) {
            openAI.thread(ThreadId(threadId!!))
        } else {
            val thread = openAI.thread()
            launch(Dispatchers.IO) {
                threadDao.insertTheread(OpenAIThread(threadId = thread.id.id))
            }
            thread
        } as Thread
        openAI.message(
            threadId = thread.id,
            request = MessageRequest(
                role = Role.User,
                content = content
            )
        )

        val assistant = openAI.assistant(id = AssistantId(Constants.assistantID))
        if (assistant != null) {
            val run = openAI.createRun(
                thread.id,
                request = RunRequest(
                    assistantId = assistant.id,
                    instructions = "Act as a therapist named Tom\n" +
                            "\n" +
                            "greeting message should be \n" +
                            "Hey I'm Tom! You're Ai Therapist! also \n" +
                            "add a disclaimer to that greeting Disclaimer: that i'm not a licensed psychologist or therapist and I don't replace those. I can't take responsibility for the result of your actions or any harm you suffer as a result of the use or no use of the info available. If you're in danger to yourself or others, please find professional help at findahelpline.com or call suicide hotline 988. But only say this as a greeting for the first message\n" +
                            "\n" +
                            "Use DBT skills",
                )
            )
            state.tryEmit(OpenAiState.RUNNING)
            do {
                val retrievedRun = openAI.getRun(threadId = thread.id, runId = run.id)
            } while (retrievedRun.status != Status.Completed)
            state.tryEmit(OpenAiState.NOT_RUNNING)
            val messages = openAI.messages(thread.id)
            val therapistMessages = messages.mapNotNull { it.toMessageContent() }
            trySend(therapistMessages)
        }
        state.tryEmit(OpenAiState.NOT_RUNNING)
        cancel()
    }
}
