package dev.kobietka.flashcards

import android.os.Bundle
import dev.kobietka.flashcards.presentation.ui.common.BaseActivity
import dev.kobietka.flashcards.presentation.ui.common.ClickInfo
import dev.kobietka.flashcards.presentation.ui.fragmenteditlist.EditListFragment
import dev.kobietka.flashcards.presentation.ui.fragmentmain.MainFragment
import dev.kobietka.flashcards.presentation.ui.fragmentplay.PlayFragment
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject


class MainActivity : BaseActivity() {

    @Inject lateinit var launchEvents: Observable<ClickInfo>
    val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        presentationComponent.inject(this)
        supportFragmentManager.beginTransaction()
            .add(R.id.main_container, MainFragment())
            .commit()
        compositeDisposable.add(
            launchEvents.subscribe(this::launch)
        )
    }

    fun launch(clickInfo: ClickInfo) {
        if(clickInfo.clickId == 0){
            supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.exit_right_to_left, R.anim.enter_right_to_left)
                .replace(R.id.main_container, PlayFragment())
                .addToBackStack(null)
                .commit()
        } else if (clickInfo.clickId == 1){
            supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.exit_right_to_left, R.anim.enter_right_to_left)
                .replace(R.id.main_container, EditListFragment())
                .addToBackStack(null)
                .commit()
        }
    }
}
