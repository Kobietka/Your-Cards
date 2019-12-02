package dev.kobietka.flashcards.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [CardListEntity::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
        abstract fun cardListDao(): CardListDao
}