package dev.kobietka.flashcards.di

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import io.reactivex.subjects.BehaviorSubject
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
    @Named("TogglSubject")
    fun providesTogglEventsSubject(): Subject<Int> {
        return BehaviorSubject.create<Int>().toSerialized()
    }

    @Singleton
    @Provides
    @Named("PlaySubject")
    fun providePlayEventsSubject(): Subject<Int> {
        return BehaviorSubject.create<Int>().toSerialized()
    }

}