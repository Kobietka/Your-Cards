package dev.kobietka.flashcards.presentation.ui

import androidx.appcompat.app.AppCompatActivity
import dev.kobietka.flashcards.App
import dev.kobietka.flashcards.di.PresentationComponent
import dev.kobietka.flashcards.di.PresentationModule


abstract class BaseActivity : AppCompatActivity() {
    protected val presentationComponent: PresentationComponent by lazy {
        (application as App).applicationComponent.presentationComponent(PresentationModule(this))
    }
}