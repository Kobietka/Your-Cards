package dev.kobietka.flashcards.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import io.reactivex.Observable

@Dao
interface FlashcardDao {

    @Query("SELECT * from flashcards")
    fun getAllCards(): List<FlashcardEntity>

    @Insert
    fun insertFlashcard(flashcardEntity: FlashcardEntity)

    @Query("Delete from flashcards")
    fun deleteAllFlashcards()

    @Query("SELECT id from flashcards")
    fun getAllIds(): Observable<List<Int>>

    @Query("SELECT * FROM flashcards where id = :id")
    fun getById(id: Int): Observable<FlashcardEntity>

    @Delete
    fun deleteOneFlashcard(flashcardEntity: FlashcardEntity)
}