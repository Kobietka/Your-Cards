package dev.kobietka.flashcards.presentation.ui.fragmenteditlist

import io.reactivex.Observable
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.widget.SwitchCompat
import dev.kobietka.flashcards.R
import dev.kobietka.flashcards.data.CardListDao
import dev.kobietka.flashcards.presentation.ui.common.BaseFragment
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class EditListFragment: BaseFragment() {

    @Inject lateinit var launchEvents: Observable<Int>
    @Inject lateinit var listDao: CardListDao
    val compositeDisposable = CompositeDisposable()

    

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presentationComponent.inject(this)

        compositeDisposable.add(
            launchEvents.flatMapMaybe {
                listDao.findById(it)
            }.subscribe {
                view.findViewById<EditText>(R.id.text_list_name_edit).setText(it.name)
                view.findViewById<SwitchCompat>(R.id.switch_endless_edit).isChecked = it.endless
                view.findViewById<SwitchCompat>(R.id.switch_random_order_edit).isChecked = it.randomOrder
                view.findViewById<SwitchCompat>(R.id.switch_typing_edit).isChecked = it.typingAnswer
            }
        )
    }

    override fun getLayout(): Int {
        return R.layout.fragment_edit_list
    }


}