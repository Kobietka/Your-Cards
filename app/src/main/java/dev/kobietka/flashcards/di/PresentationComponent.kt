package dev.kobietka.flashcards.di

import dagger.Subcomponent
import dev.kobietka.flashcards.MainActivity
import dev.kobietka.flashcards.presentation.ui.fragmentmain.MainFragment

@Subcomponent(modules = [PresentationModule::class])
interface PresentationComponent {
    fun inject(mainActivity: MainActivity)
    fun inject(mainFragment: MainFragment)
}