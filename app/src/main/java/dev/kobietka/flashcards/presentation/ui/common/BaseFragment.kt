package dev.kobietka.flashcards.presentation.ui.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dev.kobietka.flashcards.App
import dev.kobietka.flashcards.di.PresentationComponent
import dev.kobietka.flashcards.di.PresentationModule

abstract class BaseFragment: Fragment() {

    val presentationComponent: PresentationComponent by lazy {
        App.applicationComponent.presentationComponent(PresentationModule(baseFragment = this))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(getLayout(), container, false)
    }

    abstract fun getLayout(): Int

}