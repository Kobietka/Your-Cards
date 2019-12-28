package dev.kobietka.flashcards.presentation.ui.fragmenteditlist

import io.reactivex.Observable
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.widget.SwitchCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dev.kobietka.flashcards.R
import dev.kobietka.flashcards.data.CardListDao
import dev.kobietka.flashcards.data.FlashcardDao
import dev.kobietka.flashcards.data.FlashcardEntity
import dev.kobietka.flashcards.presentation.ui.common.BaseFragment
import dev.kobietka.flashcards.presentation.ui.common.ClickInfo
import dev.kobietka.flashcards.presentation.ui.fragmentmain.MainFragment
import dev.kobietka.flashcards.presentation.ui.rvs.FlashcardEditAdapter
import dev.kobietka.flashcards.presentation.viewmodel.EditListViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class EditListFragment: BaseFragment() {

    @Inject lateinit var launchEvents: Observable<ClickInfo>
    @Inject lateinit var listDao: CardListDao
    @Inject lateinit var flashcardDao: FlashcardDao
    @Inject lateinit var adapter: FlashcardEditAdapter
    @Inject lateinit var editListViewModel: EditListViewModel
    val compositeDisposable = CompositeDisposable()

    lateinit var recyclerView: RecyclerView

    var listId = 0

    lateinit var closeButton: ImageButton
    lateinit var addButton: Button
    lateinit var saveButton: Button
    lateinit var listNameEditText: EditText
    lateinit var endlessSwitch: SwitchCompat
    lateinit var randomSwitch: SwitchCompat
    lateinit var typingSwitch: SwitchCompat
    lateinit var shownWord: EditText
    lateinit var hiddenWord: EditText

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presentationComponent.inject(this)

        listNameEditText = view.findViewById(R.id.text_list_name_edit)
        endlessSwitch = view.findViewById(R.id.switch_endless_edit)
        randomSwitch = view.findViewById(R.id.switch_random_order_edit)
        typingSwitch = view.findViewById(R.id.switch_typing_edit)
        shownWord = view.findViewById(R.id.text_shown_word_edit)
        hiddenWord = view.findViewById(R.id.text_hidden_word_edit)
        addButton = view.findViewById(R.id.button_add_flashcard_edit)
        closeButton = view.findViewById(R.id.button_cancel_edit_list)
        saveButton = view.findViewById(R.id.button_save_list)

        recyclerView = view.findViewById(R.id.recycler_edit_list_edit)
        recyclerView.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        recyclerView.adapter = adapter

        compositeDisposable.add(
            launchEvents.flatMapMaybe {
                listDao.findById(it.listId)
            }.subscribe {
                editListViewModel.setID(it.id!!)
                adapter.listId = it.id!!
            }
        )

        compositeDisposable.add(
            editListViewModel.listName.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    listNameEditText.setText(it)
                }
        )

        compositeDisposable.add(
            editListViewModel.typing.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    typingSwitch.isChecked = it
                }
        )

        compositeDisposable.add(
            editListViewModel.random.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    randomSwitch.isChecked = it
                }
        )


        saveButton.setOnClickListener {
            editListViewModel.saveList(
                listNameEditText.text.toString(),
                endlessSwitch.isChecked,
                typingSwitch.isChecked,
                randomSwitch.isChecked
            )

            val fragment = activity!!.supportFragmentManager.findFragmentByTag("mainFragment")
            activity!!.supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.exit_right_to_left, R.anim.enter_right_to_left)
                .replace(R.id.main_container, fragment!!)
                .commit()
        }

        addButton.setOnClickListener {
            editListViewModel.addFlashcard(
                shownWord.text.toString(),
                hiddenWord.text.toString()
            )
            clearEditTexts()
        }

        closeButton.setOnClickListener {
            val fragment = activity!!.supportFragmentManager.findFragmentByTag("mainFragment")
            activity!!.supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.exit_right_to_left, R.anim.enter_right_to_left)
                .replace(R.id.main_container, fragment!!)
                .commit()
        }
    }

    private fun clearEditTexts(){
        shownWord.text.clear()
        hiddenWord.text.clear()
    }

    override fun getLayout(): Int {
        return R.layout.fragment_edit_list
    }

}