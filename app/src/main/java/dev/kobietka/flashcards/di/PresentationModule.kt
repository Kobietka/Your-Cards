package dev.kobietka.flashcards.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dev.kobietka.flashcards.presentation.ui.common.BaseActivity
import dev.kobietka.flashcards.presentation.ui.common.BaseFragment
import javax.inject.Named

@Module
class PresentationModule(private val baseActivity: BaseActivity? = null, private val baseFragment: BaseFragment? = null) {

    @Provides
    @Named("ActivityContext")
    fun provideActivityContext(): Context?{
        return baseActivity
    }

    @Provides
    @Named("BaseFragment")
    fun provideBaseFragment(): BaseFragment?{
        return baseFragment
    }
}