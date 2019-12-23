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
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class EditListFragment: BaseFragment() {

    @Inject lateinit var launchEvents: Observable<ClickInfo>
    @Inject lateinit var listDao: CardListDao
    @Inject lateinit var flashcardDao: FlashcardDao
    val compositeDisposable = CompositeDisposable()

    lateinit var recyclerView: RecyclerView
    @Inject lateinit var adapter: FlashcardEditAdapter

    var listId = 0
    var flashcardsCount = 0

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

        compositeDisposable.add(
            launchEvents.flatMapMaybe {
                listDao.findById(it.listId)
            }.subscribe {
                listId = it.id!!
                listNameEditText.setText(it.name)
                flashcardsCount = it.count
                endlessSwitch.isChecked = it.endless
                randomSwitch.isChecked = it.randomOrder
                typingSwitch.isChecked = it.typingAnswer
            }
        )

        recyclerView = view.findViewById(R.id.recycler_edit_list_edit)
        recyclerView.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        recyclerView.adapter = adapter
        adapter.setCardListId(listId)

        addButton = view.findViewById(R.id.button_add_flashcard_edit)
        closeButton = view.findViewById(R.id.button_cancel_edit_list)
        saveButton = view.findViewById(R.id.button_save_list)

        saveButton.setOnClickListener {
            updateList(listId)

            activity?.supportFragmentManager?.beginTransaction()
                ?.setCustomAnimations(R.anim.exit_right_to_left, R.anim.enter_right_to_left)
                ?.replace(R.id.main_container, MainFragment())
                ?.addToBackStack(null)
                ?.commit()
        }

        addButton.setOnClickListener {
            flashcardsCount++
            flashcardDao.insertFlashcard(
                FlashcardEntity(
                    null,
                    shownWord.text.toString(),
                    hiddenWord.text.toString(),
                    listId
                )
            ).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::clearEditTexts)
        }

        closeButton.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()
                ?.setCustomAnimations(R.anim.enter_top_to_bottom, R.anim.exit_top_to_bottom)
                ?.replace(R.id.main_container, MainFragment())
                ?.addToBackStack(null)
                ?.commit()
        }
    }

    private fun updateList(oldId: Int){
        listDao.updateListName(oldId, listNameEditText.text.toString())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
        listDao.updateListCount(oldId, flashcardsCount)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
        listDao.updateListEndless(oldId, endlessSwitch.isChecked)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
        listDao.updateListRandom(oldId, randomSwitch.isChecked)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
        listDao.updateListTyping(oldId, typingSwitch.isChecked)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
    }

    private fun clearEditTexts(){
        shownWord.text.clear()
        hiddenWord.text.clear()
    }

    override fun getLayout(): Int {
        return R.layout.fragment_edit_list
    }

}