package dev.kobietka.flashcards.data

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

@Dao
interface FlashcardDao {

    @Query("SELECT * from flashcards")
    fun getAllCards(): Observable<List<FlashcardEntity>>

    @Insert
    fun insertFlashcard(flashcardEntity: FlashcardEntity): Completable

    @Query("Delete from flashcards")
    fun deleteAllFlashcards()

    @Query("SELECT id from flashcards")
    fun getAllIds(): Observable<List<Int>>

    @Query("SELECT * FROM flashcards where id = :id")
    fun getById(id: Int): Observable<FlashcardEntity>

    @Query("DELETE FROM flashcards where id = :id")
    fun deleteById(id: Int?): Completable

    @Query("SELECT * FROM flashcards where listId = :id")
    fun getAllFlashcardsByListId(id: Int): Observable<List<FlashcardEntity>>

    @Delete
    fun deleteOneFlashcard(flashcardEntity: FlashcardEntity)

    @Query("SELECT COUNT(*) from flashcards WHERE listId = :id")
    fun getCardsCountByListId(id: Int): Observable<Int>

}