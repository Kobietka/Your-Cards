package dev.kobietka.flashcards.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [CardListEntity::class, FlashcardEntity::class], version = 7)
abstract class AppDatabase: RoomDatabase() {
        abstract fun cardListDao(): CardListDao
        abstract fun flashcardDao(): FlashcardDao
}