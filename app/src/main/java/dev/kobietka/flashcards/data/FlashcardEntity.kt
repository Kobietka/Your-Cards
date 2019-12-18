package dev.kobietka.flashcards.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "flashcards")
data class FlashcardEntity(@PrimaryKey(autoGenerate = true) val id: Int?,
                           @ColumnInfo val shownWord: String,
                           @ColumnInfo val hiddenWord: String,
                           @ColumnInfo val listId: Int)