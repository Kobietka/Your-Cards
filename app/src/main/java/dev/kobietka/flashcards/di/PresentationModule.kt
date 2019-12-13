package dev.kobietka.flashcards.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dev.kobietka.flashcards.presentation.ui.common.BaseActivity
import javax.inject.Named

@Module
class PresentationModule(private val baseActivity: BaseActivity) {

    @Provides
    @Named("ActivityContext")
    fun provideActivityContext(): Context{
        return baseActivity
    }
}