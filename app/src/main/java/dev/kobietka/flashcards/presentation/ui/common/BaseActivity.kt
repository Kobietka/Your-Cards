package dev.kobietka.flashcards.presentation.ui.common

import androidx.appcompat.app.AppCompatActivity
import dev.kobietka.flashcards.App
import dev.kobietka.flashcards.di.PresentationComponent
import dev.kobietka.flashcards.di.PresentationModule


abstract class BaseActivity : AppCompatActivity() {

    //Setting up the presentation component
    protected val presentationComponent: PresentationComponent by lazy {
        App.applicationComponent.presentationComponent(PresentationModule(this))
    }
}