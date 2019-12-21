package dev.kobietka.flashcards.di

import dagger.Subcomponent
import dev.kobietka.flashcards.MainActivity
import dev.kobietka.flashcards.presentation.ui.fragmentaddlist.AddListFragment
import dev.kobietka.flashcards.presentation.ui.fragmenteditlist.EditListFragment
import dev.kobietka.flashcards.presentation.ui.fragmentmain.MainFragment
import dev.kobietka.flashcards.presentation.ui.fragmentplay.PlayFragment

@Subcomponent(modules = [PresentationModule::class])
interface PresentationComponent {
    fun inject(mainActivity: MainActivity)
    fun inject(mainFragment: MainFragment)
    fun inject(addListFragment: AddListFragment)
    fun inject(editListFragment: EditListFragment)
    fun inject(playFragment: PlayFragment)
}