package com.devlanding.aitherapist.data

import com.aallam.openai.api.BetaOpenAI
import com.aallam.openai.api.core.Role
import com.aallam.openai.api.message.Message

data class TherapistMessage(val message: String, val messageType: MessageType) {
}


@OptIn(BetaOpenAI::class)
fun Message.toMessageContent(): TherapistMessage? {
    val msg = this.content.first() as? com.aallam.openai.api.message.MessageContent.Text
    val msgText = msg?.text?.value
    val messageType = if (this.role == Role.User) {
        MessageType.HUMAN
    } else if (this.role == Role.Assistant) {
        MessageType.BOT
    } else {
        MessageType.HUMAN
    }
    return if (msgText != null) {
        TherapistMessage(msgText, messageType)
    } else {
        null
    }
}