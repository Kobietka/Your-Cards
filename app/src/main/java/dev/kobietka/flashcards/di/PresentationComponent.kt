package dev.kobietka.flashcards.di

import dagger.Subcomponent
import dev.kobietka.flashcards.MainActivity

@Subcomponent(modules = [PresentationModule::class])
interface PresentationComponent {
    fun inject(mainActivity: MainActivity)
}