package dev.kobietka.flashcards.presentation.ui.fragmentplay

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.view.isGone
import dev.kobietka.flashcards.R
import dev.kobietka.flashcards.data.CardListDao
import dev.kobietka.flashcards.data.FlashcardDao
import dev.kobietka.flashcards.data.FlashcardEntity
import dev.kobietka.flashcards.presentation.ui.common.BaseFragment
import dev.kobietka.flashcards.presentation.ui.common.ClickInfo
import dev.kobietka.flashcards.presentation.ui.fragmentmain.MainFragment
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.w3c.dom.Text
import java.util.*
import javax.inject.Inject

class PlayFragment : BaseFragment() {

    private lateinit var closeButton: ImageView
    private lateinit var hiddenWord: RelativeLayout
    private lateinit var correctButton: RelativeLayout
    private lateinit var notCorrectButton: RelativeLayout
    private lateinit var listName: TextView
    private lateinit var hiddenWordText: TextView
    private lateinit var shownWord: TextView
    private lateinit var countText: TextView
    private lateinit var enterText: EditText
    private lateinit var endText: TextView
    private lateinit var endButton: RelativeLayout

    private var hiddenClicked = false
    private var listId = 0
    private var cardCount = 0
    private var count = 0
    private var random = false
    private var typing = false
    private var endless = false
    private var wasShuffled = false
    private var flashcardList = listOf<FlashcardEntity>()

    private var correctAnswers = 0

    @Inject lateinit var flashcardDao: FlashcardDao
    @Inject lateinit var listDao: CardListDao
    @Inject lateinit var events: Observable<ClickInfo>
    val compositeDisposable = CompositeDisposable()

    @SuppressLint("CheckResult")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presentationComponent.inject(this)

        closeButton = view.findViewById(R.id.play_close)
        hiddenWord = view.findViewById(R.id.play_hidden_word)
        correctButton = view.findViewById(R.id.play_correct)
        notCorrectButton = view.findViewById(R.id.play_not_correct)
        listName = view.findViewById(R.id.play_list_name)
        shownWord = view.findViewById(R.id.play_shown_word)
        hiddenWordText = view.findViewById(R.id.play_hidden_word_text)
        countText = view.findViewById(R.id.play_count)
        enterText = view.findViewById(R.id.play_enter_word)
        endText = view.findViewById(R.id.play_end_text)
        endButton = view.findViewById(R.id.play_end_button)

        compositeDisposable.add(
            events.flatMapMaybe {
                Log.e("LISTID", it.listId.toString())
                listDao.findById(it.listId)
            }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    listName.text = it.name
                    listId = it.id!!
                    cardCount = it.count
                    random = it.randomOrder
                    endless = it.endless
                    typing = it.typingAnswer
                    flashcardDao.getAllFlashcardsByListId(it.id)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::setNewFlashcardList)
                }
        )

        hiddenWordText.text = "Click here to start"


        correctButton.setOnClickListener {
            onCorrectAnswer()
        }

        notCorrectButton.setOnClickListener {
            onNotCorrectAnswer()
        }

        hiddenWord.setOnClickListener {
            if(!wasShuffled) {
                if(random) flashcardList = flashcardList.shuffled(Random(35446))
                wasShuffled = true
            }
            if(endless) {
                endText.isGone = false
                notCorrectButton.isGone = true
                endButton.isGone = false
            }
            onHiddenClick()
        }

        endButton.setOnClickListener {

        }

        closeButton.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.main_container, MainFragment())
                ?.commit()
        }

    }

    private fun onCorrectAnswer(){
        count++
        if(!typing) {
            if(endless && count == cardCount) {
                count = 0
                if(random) flashcardList = flashcardList.shuffled(Random(245657345))
            }
            if (count < cardCount) {
                shownWord.text = flashcardList[count].shownWord
                hiddenWordText.text = "Tap here to check"
                countText.text = (count + 1).toString() + "/" + cardCount.toString()
            } else {
                activity!!.supportFragmentManager.beginTransaction()
                    .replace(R.id.main_container, MainFragment())
                    .commit()
            }
        } else {
            if(flashcardList[count - 1].hiddenWord == enterText.text.toString()) correctAnswers++
            Log.e(flashcardList[count - 1].hiddenWord,"HIDDEN")
            Log.e(enterText.text.toString(), "ENTERED")
            Log.e("COUNT", count.toString())
            Log.e("CORRECT", correctAnswers.toString())
            if(endless && count == cardCount) {
                count = 0
                if(random) flashcardList = flashcardList.shuffled(Random(245657345))
            }
            if(count < cardCount){
                shownWord.text = flashcardList[count].shownWord
                countText.text = (count + 1).toString() + "/" + cardCount.toString()
            } else {
                Log.e("CORRECT", correctAnswers.toString())
                activity!!.supportFragmentManager.beginTransaction()
                    .replace(R.id.main_container, MainFragment())
                    .commit()
            }
        }
    }

    private fun onNotCorrectAnswer(){
        count++
        if(endless && count == cardCount) {
            count = 0
            if(random) flashcardList = flashcardList.shuffled(Random(245657345))
        }
        if (count < cardCount) {
            shownWord.text = flashcardList[count].shownWord
            hiddenWordText.text = "Tap here to check"
            countText.text = (count + 1).toString() + "/" + cardCount.toString()
        } else {
            activity!!.supportFragmentManager.beginTransaction()
                .replace(R.id.main_container, MainFragment())
                .commit()
        }
    }

    private fun onHiddenClick(){
        cardCount = flashcardList.size
        if(typing) {
            //if(random) flashcardList = flashcardList.shuffled(Random(466754))
            shownWord.text = flashcardList[count].shownWord
            countText.text = (count + 1).toString() + "/" + cardCount.toString()
            hiddenWordText.isGone = true
            enterText.isGone = false
        } else if(!typing){
            hiddenWordText.text = "Tap here to check"
            if(count < cardCount){
                if (!hiddenClicked) {
                    shownWord.text = flashcardList[count].shownWord
                    countText.text = (count + 1).toString() + "/" + cardCount.toString()
                    hiddenClicked = true
                } else {
                    hiddenWordText.text = flashcardList[count].hiddenWord
                    hiddenClicked = false
                }
            } else {
                hiddenWordText.text = flashcardList[count].hiddenWord
            }
        } else {
            if(count < cardCount){
                shownWord.text = flashcardList[count].shownWord
                countText.text = (count + 1).toString() + "/" + cardCount.toString()
            }
        }
    }

    private fun setNewFlashcardList(list: List<FlashcardEntity>){
        flashcardList = list
    }

    override fun getLayout(): Int {
        return R.layout.fragment_play
    }

}
