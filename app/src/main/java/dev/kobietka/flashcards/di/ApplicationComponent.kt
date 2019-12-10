package dev.kobietka.flashcards.di

import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationModule::class, DatabaseModule::class])
interface ApplicationComponent{
    fun presentationComponent(presentationModule: PresentationModule): PresentationComponent
}
