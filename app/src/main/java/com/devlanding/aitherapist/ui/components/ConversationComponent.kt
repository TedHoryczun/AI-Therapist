package com.devlanding.aitherapist.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.aallam.openai.api.BetaOpenAI
import com.devlanding.aitherapist.R
import com.devlanding.aitherapist.data.MessageType
import com.devlanding.aitherapist.data.TherapistMessage
import com.devlanding.aitherapist.ui.theme.AITherapistTheme

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun PreviewConversation() {
    AITherapistTheme {
        val msgs = listOf(
            TherapistMessage("Hi", MessageType.HUMAN),
            TherapistMessage("good morning", MessageType.BOT)
        )
        ConversationComponent(messages = msgs)
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ConversationComponent(
    messages: List<TherapistMessage>,
    sendMessage: (message: String) -> Unit = {}
) {
    ConstraintLayout(Modifier.fillMaxHeight()) {
        val (bottomTextField, conversation) = createRefs()

        Box(
            Modifier
                .constrainAs(conversation) { bottom.linkTo(bottomTextField.top) }
                .padding(bottom = 16.dp, top = 16.dp, start = 8.dp, end = 8.dp)) {
            LazyColumn(
                Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .align(Alignment.BottomCenter),
                reverseLayout = true
            ) {
                items(messages.size) { index ->
                    val msg = messages[index]
                    Spacer(modifier = Modifier.height(8.dp))
                    Box(Modifier.fillMaxWidth()) {
                        MessageBubble(
                            Modifier.align(Alignment.CenterEnd),
                            msg.message,
                            msg.messageType
                        )
                    }
                }
            }
        }
        Box(Modifier.constrainAs(bottomTextField) {
            bottom.linkTo(parent.bottom)
        }) {
            var messageField by remember { mutableStateOf("") }
            val keyboardController = LocalSoftwareKeyboardController.current


            TextField(modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
                textStyle = MaterialTheme.typography.bodyMedium,
                value = messageField,
                placeholder = { Text(text = "Message") },
                onValueChange = { messageField = it },
                trailingIcon = {
                    Icon(
                        modifier = Modifier.clickable {
                            sendMessage(messageField)
                            messageField = ""
                            keyboardController?.hide()
                        },
                        painter = painterResource(id = R.drawable.send_icon),
                        contentDescription = "Send"
                    )
                })
        }
    }
}

@Composable
fun MessageBubble(
    modifier: Modifier,
    message: String,
    messageType: MessageType
) {
    val bubbleColor = if (messageType == MessageType.HUMAN) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.secondary
    }
    Box(
        modifier
            .clip(RoundedCornerShape(16.dp))
            .background(color = bubbleColor)
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun PreviewMessageBubble() {
    AITherapistTheme {

        MessageBubble(
            Modifier,
            "Hello",
            MessageType.BOT
        )
    }

}

