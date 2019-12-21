package dev.kobietka.flashcards.presentation.ui.fragmentaddlist

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.widget.SwitchCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dev.kobietka.flashcards.R
import dev.kobietka.flashcards.data.CardListDao
import dev.kobietka.flashcards.data.CardListEntity
import dev.kobietka.flashcards.data.FlashcardDao
import dev.kobietka.flashcards.data.FlashcardEntity
import dev.kobietka.flashcards.presentation.ui.common.BaseFragment
import dev.kobietka.flashcards.presentation.ui.fragmentmain.MainFragment
import dev.kobietka.flashcards.presentation.ui.rvs.FlashcardAdapter
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class AddListFragment : BaseFragment() {

    lateinit var cancelButton: ImageButton
    lateinit var saveButton: Button
    lateinit var listName: EditText
    lateinit var shownWord: EditText
    lateinit var hiddenWord: EditText
    lateinit var addFlashcardButton: Button
    lateinit var endlessSwitch: SwitchCompat
    lateinit var randomSwitch: SwitchCompat
    lateinit var typingSwitch: SwitchCompat
    lateinit var createButton: Button

    var listId: Long = 0

    lateinit var recyclerView: RecyclerView
    @Inject lateinit var adapter: FlashcardAdapter
    @Inject lateinit var listDao: CardListDao
    @Inject lateinit var flashcardDao: FlashcardDao

    var flashcardsCount = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presentationComponent.inject(this)

        recyclerView = view.findViewById(R.id.recycler_add_list)
        recyclerView.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        recyclerView.adapter = adapter

        createButton = view.findViewById(R.id.button_create_list)
        cancelButton = view.findViewById(R.id.button_cancel_add_list)
        saveButton = view.findViewById(R.id.button_save_list)
        endlessSwitch = view.findViewById(R.id.switch_endless)
        randomSwitch = view.findViewById(R.id.switch_random_order)
        typingSwitch = view.findViewById(R.id.switch_typing)
        listName = view.findViewById(R.id.text_list_name)
        addFlashcardButton = view.findViewById(R.id.button_add_flashcard)
        shownWord = view.findViewById(R.id.text_shown_word)
        hiddenWord = view.findViewById(R.id.text_hidden_word)

        createButton.setOnClickListener {
            val newID = listDao.insertList(CardListEntity(
                null,
                listName.text.toString(),
                flashcardsCount,
                endlessSwitch.isChecked,
                typingSwitch.isChecked,
                randomSwitch.isChecked
            ))
            setId(newID)

            Log.e("LISTID", newID.toString())
            adapter.setCardListId(listId.toInt())

            saveButton.isGone = false
            createButton.isGone = true
            addFlashcardButton.isGone = false
            view.findViewById<ImageView>(R.id.arrow_shown_word).isGone = false
            view.findViewById<ImageView>(R.id.arrow_hidden_word).isGone = false
            shownWord.isGone = false
            hiddenWord.isGone = false
        }

        saveButton.setOnClickListener {
            updateList(listId.toInt())
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.main_container, MainFragment())
                ?.addToBackStack(null)
                ?.commit()
        }

        addFlashcardButton.setOnClickListener {
            flashcardsCount++
            flashcardDao.insertFlashcard(
                FlashcardEntity(
                null,
                shownWord.text.toString(),
                hiddenWord.text.toString(),
                listId.toInt()
            )).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
        }

        cancelButton.setOnClickListener {

            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.main_container, MainFragment())
                ?.addToBackStack(null)
                ?.commit()
        }
    }

    override fun getLayout(): Int {
        return R.layout.fragment_add_list
    }

    private fun updateList(oldId: Int){
        listDao.updateListName(oldId, listName.text.toString())
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

    private fun setId(id: Long){
        listId = id
    }

}