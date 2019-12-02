package dev.kobietka.flashcards.di

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dev.kobietka.flashcards.data.AppDatabase
import dev.kobietka.flashcards.data.CardListDao
import dev.kobietka.flashcards.data.FlashcardDao
import javax.inject.Named
import javax.inject.Singleton

@Module
class DatabaseModule(@Named("ApplicationContext") val context: Application) {

    @Provides
    @Singleton
    fun provideDatabase(): AppDatabase{
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "app_database"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    fun provideCardListDao(appDatabase: AppDatabase): CardListDao {
        return appDatabase.cardListDao()
    }

    @Provides
    fun provideFlashcardDao(appDatabase: AppDatabase): FlashcardDao {
        return appDatabase.flashcardDao()
    }
}