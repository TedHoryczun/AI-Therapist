package com.devlanding.aitherapist.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devlanding.aitherapist.data.MessageType
import com.devlanding.aitherapist.data.OpenAIRepository
import com.devlanding.aitherapist.data.OpenAiState
import com.devlanding.aitherapist.data.TherapistMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConversationViewModel @Inject constructor(val openAi: OpenAIRepository) : ViewModel() {
    val messages: StateFlow<List<TherapistMessage>> get() = _messages.asStateFlow()
    private val _messages: MutableStateFlow<List<TherapistMessage>> =
        MutableStateFlow(listOf())
    val openAiState get() = _openAiState.asStateFlow()
    private val _openAiState = MutableStateFlow(OpenAiState.NOT_RUNNING)

    fun initialize() {
        viewModelScope.launch(Dispatchers.IO) {
            openAi.initialize()
            openAi.getMessages().collectLatest { msgs ->
                _messages.value = msgs
            }
        }
        viewModelScope.launch {
            openAi.state.collectLatest { state ->
                _openAiState.value = state

            }
        }
    }

    fun sendMessage(message: String) {
        if (openAiState.value == OpenAiState.NOT_RUNNING) {
            val therapistMessage = TherapistMessage(message, MessageType.HUMAN)
            addToMessages(therapistMessage)
            viewModelScope.launch(Dispatchers.IO) {
                openAi.virtualAssistant(message).onCompletion { }.collectLatest {
                    _messages.value = it
                }
            }
        }
    }

    private fun addToMessages(therapistMessage: TherapistMessage) {
        val msgs = _messages.value.toMutableList()
        msgs.add(therapistMessage)
        _messages.value = msgs
    }
}