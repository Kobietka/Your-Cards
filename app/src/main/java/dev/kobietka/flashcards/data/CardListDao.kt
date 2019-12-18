package dev.kobietka.flashcards.data

import androidx.room.*
import io.reactivex.Observable
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single

@Dao
interface CardListDao {

    @Query("SELECT * from cardList")
    fun getAllLists(): Observable<List<CardListEntity>>

    @Insert
    fun insertList(cardList: CardListEntity): Long

    @Query("DELETE from cardList")
    fun deleteAllLists()

    @Delete
    fun deleteOneList(cardList: CardListEntity): Completable

    @Query("DELETE FROM cardList where id = :id")
    fun deleteById(id: Int): Completable

    @Query("SELECT id from cardList")
    fun getIdsList(): Observable<List<Int>>

    @Query("SELECT * FROM cardList where id = :id")
    fun getById(id: Int): Observable<CardListEntity>

    @Query("SELECT * FROM cardList where id = :id")
    fun findById(id: Int): Maybe<CardListEntity>

    @Query("UPDATE cardList SET name = :name WHERE id = :id")
    fun updateListName(id: Int, name: String): Completable

    @Query("UPDATE cardList SET count = :count WHERE id = :id")
    fun updateListCount(id: Int, count: Int): Completable

    @Query("UPDATE cardList SET endless = :endless WHERE id = :id")
    fun updateListEndless(id: Int, endless: Boolean): Completable

    @Query("UPDATE cardList SET typingAnswer = :typing WHERE id = :id")
    fun updateListTyping(id: Int, typing: Boolean): Completable

    @Query("UPDATE cardList SET randomOrder = :random WHERE id = :id")
    fun updateListRandom(id: Int, random: Boolean): Completable

}