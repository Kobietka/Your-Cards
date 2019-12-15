package dev.kobietka.flashcards.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dev.kobietka.flashcards.data.AppDatabase
import dev.kobietka.flashcards.data.CardListDao
import dev.kobietka.flashcards.data.FlashcardDao
import javax.inject.Named
import javax.inject.Singleton

@Module(includes = [ApplicationModule::class])
class DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@Named("ApplicationContext") context: Context): AppDatabase{
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "app_database"
        ).allowMainThreadQueries().fallbackToDestructiveMigration().build()
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