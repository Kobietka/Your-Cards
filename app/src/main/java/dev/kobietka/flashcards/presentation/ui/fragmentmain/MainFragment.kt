package dev.kobietka.flashcards.presentation.ui.fragmentmain

import android.os.Bundle
import android.view.View
import dev.kobietka.flashcards.R
import dev.kobietka.flashcards.presentation.ui.common.BaseFragment

class MainFragment: BaseFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun getLayout(): Int {
        return R.layout.fragment_main
    }

}