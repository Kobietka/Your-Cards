package dev.kobietka.flashcards.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cardList")
data class CardListEntity(@PrimaryKey(autoGenerate = true) val id: Int?,
                          @ColumnInfo val name: String,
                          @ColumnInfo val endless: Boolean,
                          @ColumnInfo val typingAnswer: Boolean,
                          @ColumnInfo val randomOrder: Boolean)


