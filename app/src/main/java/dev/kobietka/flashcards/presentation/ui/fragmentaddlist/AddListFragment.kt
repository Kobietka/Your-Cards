package dev.kobietka.flashcards.presentation.ui.fragmentaddlist

import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.widget.SwitchCompat
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
import javax.inject.Inject

class AddListFragment : BaseFragment() {

    lateinit var cancelButton: ImageButton
    lateinit var createButton: Button
    lateinit var listName: EditText
    lateinit var shownWord: EditText
    lateinit var hiddenWord: EditText
    lateinit var addFlashcardButton: Button
    lateinit var endlessSwitch: SwitchCompat
    lateinit var randomSwitch: SwitchCompat
    lateinit var typingSwitch: SwitchCompat

    lateinit var recyclerView: RecyclerView
    @Inject lateinit var adapter: FlashcardAdapter
    @Inject lateinit var listDao: CardListDao
    @Inject lateinit var flashcardDao: FlashcardDao

    var flashcardsCount = 0
    val flashcardsList = mutableListOf<FlashcardEntity>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presentationComponent.inject(this)

        recyclerView = view.findViewById(R.id.recycler_add_list)
        recyclerView.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        recyclerView.adapter = adapter

        cancelButton = view.findViewById(R.id.button_cancel_add_list)
        createButton = view.findViewById(R.id.button_create_list)
        endlessSwitch = view.findViewById(R.id.switch_endless)
        randomSwitch = view.findViewById(R.id.switch_random_order)
        typingSwitch = view.findViewById(R.id.switch_typing)
        listName = view.findViewById(R.id.text_list_name)
        addFlashcardButton = view.findViewById(R.id.button_add_flashcard)
        shownWord = view.findViewById(R.id.text_shown_word)
        hiddenWord = view.findViewById(R.id.text_hidden_word)

        createButton.setOnClickListener {
            val listNameText = listName.text.toString()
            listDao.insertList(CardListEntity(
                null,
                listNameText,
                flashcardsCount,
                endlessSwitch.isChecked,
                typingSwitch.isChecked,
                randomSwitch.isChecked
            ))

            flashcardsList.forEach {
                flashcardDao.deleteByListName(it.listName)
            }

            flashcardsList.forEach {
                flashcardDao.insertFlashcard(it.copy(listName = listNameText))
            }

            flashcardsList.clear()

            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.main_container, MainFragment())
                ?.addToBackStack(null)
                ?.commit()
        }

        addFlashcardButton.setOnClickListener {
            flashcardsCount++
            val flashcard = FlashcardEntity(
                null,
                shownWord.text.toString(),
                hiddenWord.text.toString(),
                "null"
            )

            flashcardsList.add(flashcard)
            flashcardDao.insertFlashcard(flashcard)
        }

        cancelButton.setOnClickListener {
            flashcardsList.forEach {
                flashcardDao.deleteByListName(it.listName)
            }
            flashcardsList.clear()
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.main_container, MainFragment())
                ?.addToBackStack(null)
                ?.commit()
        }
    }

    override fun getLayout(): Int {
        return R.layout.fragment_add_list
    }

}