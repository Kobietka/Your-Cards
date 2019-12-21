package dev.kobietka.flashcards.di

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import dev.kobietka.flashcards.presentation.ui.common.ClickInfo
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
    fun providesEventsSubject(): Subject<ClickInfo> {
        return BehaviorSubject.create<ClickInfo>().toSerialized()
    }

    @Singleton
    @Provides
    fun providesOtherEventsSubject(): Subject<Int>{
        return BehaviorSubject.create<Int>().toSerialized()
    }


}