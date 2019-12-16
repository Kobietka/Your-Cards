package dev.kobietka.flashcards.di

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import javax.inject.Named
import javax.inject.Singleton

@Module
class ApplicationModule(private val application: Application) {

    @Provides
    @Named("ApplicationContext")
    fun provideApplicationContext(): Context{
        return application
    }

    @Singleton
    @Provides
    fun providesLaunchEventsSubject(): Subject<Int> {
        return PublishSubject.create<Int>().toSerialized()
    }

}