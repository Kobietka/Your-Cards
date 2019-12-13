package dev.kobietka.flashcards

import android.os.Bundle
import dev.kobietka.flashcards.presentation.ui.common.BaseActivity


class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
