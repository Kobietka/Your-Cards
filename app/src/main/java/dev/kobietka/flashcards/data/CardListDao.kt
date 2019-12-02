package dev.kobietka.flashcards.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CardListDao {

    @Query("SELECT * from cardList")
    fun getAllLists(): List<CardListEntity>

    @Insert
    fun insertList(cardList: CardListEntity)

    @Query("DELETE from cardList")
    fun deleteAllLists()

    @Delete
    fun deleteOneList(cardList: CardListEntity)

}