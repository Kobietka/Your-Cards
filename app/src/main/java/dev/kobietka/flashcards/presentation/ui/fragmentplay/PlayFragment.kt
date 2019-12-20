package dev.kobietka.flashcards.presentation.ui.fragmentplay

import android.os.Bundle
import android.view.View
import dev.kobietka.flashcards.R
import dev.kobietka.flashcards.presentation.ui.common.BaseFragment

class PlayFragment : BaseFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun getLayout(): Int {
        return R.layout.fragment_play
    }
}