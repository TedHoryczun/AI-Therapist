package com.devlanding.aitherapist.data

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase

@Entity
data class OpenAIThread(
    @PrimaryKey
    @ColumnInfo(name = "thread_id") val threadId: String
)

@Dao
interface ThreadDao {
    @Query("SELECT * FROM openaithread")
    fun getThread(): List<OpenAIThread>
    @Insert
    fun insertTheread(thread: OpenAIThread)

}
@Database(entities = [OpenAIThread::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun threadDao(): ThreadDao
}


