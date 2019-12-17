package dev.kobietka.flashcards.data

import io.reactivex.Observable
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import io.reactivex.Maybe

@Dao
interface CardListDao {

    @Query("SELECT * from cardList")
    fun getAllLists(): Observable<List<CardListEntity>>

    @Insert
    fun insertList(cardList: CardListEntity)

    @Query("DELETE from cardList")
    fun deleteAllLists()

    @Delete
    fun deleteOneList(cardList: CardListEntity)

    @Query("DELETE FROM cardList where id = :id")
    fun deleteById(id: Int)

    @Query("SELECT id from cardList")
    fun getIdsList(): Observable<List<Int>>

    @Query("SELECT * FROM cardList where id = :id")
    fun getById(id: Int): Observable<CardListEntity>

    @Query("SELECT * FROM cardList where id = :id")
    fun findById(id: Int): Maybe<CardListEntity>

}