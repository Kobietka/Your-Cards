package dev.kobietka.flashcards

import android.os.Bundle
import dev.kobietka.flashcards.presentation.ui.common.BaseActivity
import dev.kobietka.flashcards.presentation.ui.fragmenteditlist.EditListFragment
import dev.kobietka.flashcards.presentation.ui.fragmentmain.MainFragment
import io.reactivex.Observable
import javax.inject.Inject


class MainActivity : BaseActivity() {

    @Inject lateinit var launchEvents: Observable<Int>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        presentationComponent.inject(this)
        supportFragmentManager.beginTransaction()
            .add(R.id.main_container, MainFragment())
            .commit()
        launchEvents.subscribe(this::launch)
    }

    fun launch(id: Int) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, EditListFragment())
            .addToBackStack(null)
            .commit()
    }
}
