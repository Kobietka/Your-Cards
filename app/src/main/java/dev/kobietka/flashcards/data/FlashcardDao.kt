package dev.kobietka.flashcards.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import io.reactivex.Observable

@Dao
interface FlashcardDao {

    @Query("SELECT * from flashcards")
    fun getAllCards(): Observable<List<FlashcardEntity>>

    @Insert
    fun insertFlashcard(flashcardEntity: FlashcardEntity)

    @Query("Delete from flashcards")
    fun deleteAllFlashcards()

    @Query("SELECT id from flashcards")
    fun getAllIds(): Observable<List<Int>>

    @Query("SELECT * FROM flashcards where id = :id")
    fun getById(id: Int): Observable<FlashcardEntity>

    @Query("DELETE FROM flashcards where id = :id")
    fun deleteById(id: Int?)

    @Query("DELETE FROM flashcards where listName = :listName")
    fun deleteByListName(listName: String)

    @Delete
    fun deleteOneFlashcard(flashcardEntity: FlashcardEntity)
}