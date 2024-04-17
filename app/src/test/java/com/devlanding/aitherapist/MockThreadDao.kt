package com.devlanding.aitherapist

import com.devlanding.aitherapist.data.OpenAIThread
import com.devlanding.aitherapist.data.ThreadDao

class MockThreadDao: ThreadDao {
    override fun getThread(): List<OpenAIThread> {
        return listOf(OpenAIThread("thread_ZBieZnPMKXfFMU6SW1AqWYS3"))
    }

    override fun insertTheread(thread: OpenAIThread) {
    }
}