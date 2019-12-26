package dev.kobietka.flashcards.presentation.ui.fragmentaddlist

import android.animation.ObjectAnimator
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.widget.SwitchCompat
import androidx.core.view.isGone
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
import io.reactivex.android.schedulers.AndroidSchedulers
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
    var flashcardsCount = 0

    lateinit var recyclerView: RecyclerView
    @Inject lateinit var adapter: FlashcardAdapter
    @Inject lateinit var listDao: CardListDao
    @Inject lateinit var flashcardDao: FlashcardDao

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

            animateShow(saveButton)
            saveButton.isGone = false

            animateHide(createButton)
            createButton.isGone = true

            animateShow(addFlashcardButton)
            addFlashcardButton.isGone = false

            view.findViewById<ImageView>(R.id.arrow_shown_word).isGone = false
            view.findViewById<ImageView>(R.id.arrow_hidden_word).isGone = false

            animateShow(shownWord)
            shownWord.isGone = false

            animateShow(hiddenWord)
            hiddenWord.isGone = false
        }

        saveButton.setOnClickListener {
            updateList(listId.toInt())
            activity?.supportFragmentManager?.beginTransaction()
                ?.setCustomAnimations(R.anim.exit_right_to_left, R.anim.enter_right_to_left)
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
                .subscribe(this::clearEditTexts)
        }

        cancelButton.setOnClickListener {

            //Deleting list when no cards were added and close button was clicked
            if(flashcardsCount == 0) listDao.deleteById(listId.toInt())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
            activity?.supportFragmentManager?.beginTransaction()
                ?.setCustomAnimations(R.anim.enter_top_to_bottom, R.anim.exit_top_to_bottom)
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

    private fun clearEditTexts(){
        shownWord.text.clear()
        hiddenWord.text.clear()
    }

    private fun setId(id: Long){
        listId = id
    }

    private fun animateShow(view: View){
        ObjectAnimator.ofFloat(view, View.ALPHA, 0f, 1f).apply {
            duration = 500
        }.start()
    }

    private fun animateHide(view: View){
        ObjectAnimator.ofFloat(view, View.ALPHA, 1f, 0f).apply {
            duration = 500
        }.start()
    }

}