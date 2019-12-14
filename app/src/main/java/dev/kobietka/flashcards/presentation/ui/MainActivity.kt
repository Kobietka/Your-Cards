package dev.kobietka.flashcards

import android.os.Bundle
import dev.kobietka.flashcards.presentation.ui.common.BaseActivity
import dev.kobietka.flashcards.presentation.ui.fragmentmain.MainFragment


class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.beginTransaction()
            .add(R.id.main_container, MainFragment())
            .commit()
    }
}
