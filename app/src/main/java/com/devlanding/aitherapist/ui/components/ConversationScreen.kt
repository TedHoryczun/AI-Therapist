package com.devlanding.aitherapist.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.devlanding.aitherapist.data.MessageType
import com.devlanding.aitherapist.data.TherapistMessage
import com.devlanding.aitherapist.ui.theme.AITherapistTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConversationScreen(
    messages: List<TherapistMessage>,
    sendMessage: (message: String) -> Unit = {}
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Column {
                        Text("AI Therapist", style = MaterialTheme.typography.titleMedium)
                        Text(
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            text = "Free Trial",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                },
            )
        },
    ) { innerPadding ->
        innerPadding
        ConversationComponent(messages = messages) { msg -> sendMessage(msg) }
    }
}

@Preview
@Composable
fun PreviewConversationScreen() {
    AITherapistTheme {
        val msgs = listOf(
            TherapistMessage("Hi", MessageType.HUMAN),
            TherapistMessage("good morning", MessageType.BOT)
        )
        ConversationScreen(messages = msgs)
    }

}