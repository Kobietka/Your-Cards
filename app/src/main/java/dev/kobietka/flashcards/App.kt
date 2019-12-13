package dev.kobietka.flashcards

import android.app.Application
import dev.kobietka.flashcards.di.ApplicationComponent
import dev.kobietka.flashcards.di.ApplicationModule
import dev.kobietka.flashcards.di.DaggerApplicationComponent

class App : Application() {

    companion object {
        lateinit var applicationComponent: ApplicationComponent
    }

    override fun onCreate() {
        super.onCreate()
        applicationComponent = DaggerApplicationComponent
            .builder()
            .applicationModule(ApplicationModule(this))
            .build()
    }
}