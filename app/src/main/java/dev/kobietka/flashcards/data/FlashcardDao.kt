package dev.kobietka.flashcards.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface FlashcardDao {

    @Query("SELECT * from flashcards")
    fun getAllCards(): List<FlashcardEntity>

    @Insert
    fun insertFlashcard(flashcardEntity: FlashcardEntity)

    @Query("Delete from flashcards")
    fun deleteAllFlashcards()

    @Delete
    fun deleteOneFlashcard(flashcardEntity: FlashcardEntity)
}