package dev.kobietka.flashcards.presentation.ui.fragmentplay

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.view.isGone
import com.google.android.material.snackbar.Snackbar
import dev.kobietka.flashcards.R
import dev.kobietka.flashcards.data.CardListDao
import dev.kobietka.flashcards.data.FlashcardDao
import dev.kobietka.flashcards.data.FlashcardEntity
import dev.kobietka.flashcards.presentation.ui.common.BaseFragment
import dev.kobietka.flashcards.presentation.ui.common.ClickInfo
import dev.kobietka.flashcards.presentation.ui.fragmentmain.MainFragment
import dev.kobietka.flashcards.presentation.ui.fragmentresults.ResultFragment
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
    private lateinit var typingCorrectButton: RelativeLayout
    private lateinit var answerIndicator: ImageView
    private lateinit var endButtonNotTyping: RelativeLayout

    private var hiddenClicked = false
    private var firstHiddenUse = true
    private var listId = 0
    private var cardCount = 0
    private var count = 0
    private var countGuessed = 0
    private var random = false
    private var typing = false
    private var endless = false
    private var wasShuffled = false
    private var firstTimeEndless = true
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
        typingCorrectButton = view.findViewById(R.id.play_correct_typing)
        answerIndicator = view.findViewById(R.id.play_answer_indicator)
        endButtonNotTyping = view.findViewById(R.id.play_end_button_not_typing)


        compositeDisposable.add(
            events.flatMapMaybe {
                Log.e("LISTID", it.listId.toString())
                listDao.findById(it.listId!!)
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
            hiddenClicked = false
            countGuessed++
            onCorrectAnswer()
        }

        notCorrectButton.setOnClickListener {
            countGuessed++
            onNotCorrectAnswer()
        }

        typingCorrectButton.setOnClickListener {
            hiddenClicked = false
            countGuessed++
            onCorrectAnswer()
        }

        hiddenWord.setOnClickListener {
            if(cardCount == 0){
                Snackbar.make(view, "Can not start, because the list is empty!", Snackbar.LENGTH_LONG).show()
            } else {
                if(!wasShuffled) {
                    val randomValue = kotlin.random.Random.nextLong(1000000, 100000000)
                    if(random) flashcardList = flashcardList.shuffled(Random(randomValue))
                    wasShuffled = true
                }
                if(endless) {
                    if(firstTimeEndless){
                        ObjectAnimator.ofFloat(endButton, View.ALPHA, 0f, 1f).apply {
                            duration = 500
                        }.start()
                        endButton.isGone = false
                        firstTimeEndless = false
                    }
                }
                onHiddenClick()
            }
        }

        endButtonNotTyping.setOnClickListener {
            val resultFragment = ResultFragment()
            resultFragment.score = correctAnswers
            if(endless) resultFragment.maxScore = countGuessed
            else resultFragment.maxScore = cardCount
            activity!!.supportFragmentManager.beginTransaction()
                ?.setCustomAnimations(R.anim.exit_right_to_left, R.anim.enter_right_to_left)
                .replace(R.id.main_container, resultFragment)
                .commit()
        }

        endButton.setOnClickListener {
            val resultFragment = ResultFragment()
            resultFragment.score = correctAnswers
            if(endless) resultFragment.maxScore = countGuessed
            else resultFragment.maxScore = cardCount
            activity!!.supportFragmentManager.beginTransaction()
                ?.setCustomAnimations(R.anim.exit_right_to_left, R.anim.enter_right_to_left)
                .replace(R.id.main_container, resultFragment)
                .commit()
        }

        closeButton.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()
                ?.setCustomAnimations(R.anim.enter_top_to_bottom, R.anim.exit_top_to_bottom)
                ?.replace(R.id.main_container, MainFragment())
                ?.commit()
        }

    }

    private fun onCorrectAnswer(){
        count++
        if(!typing) {
            correctAnswers++
            if(endless && count == cardCount) {
                count = 0
                if(random){
                    val randomValue = kotlin.random.Random.nextLong(1000000, 100000000)
                    flashcardList = flashcardList.shuffled(Random(randomValue))
                }
            }
            if (count < cardCount) {

                ObjectAnimator.ofFloat(shownWord, View.ALPHA, 1f, 0f).apply {
                    duration = 500
                }.start()
                shownWord.text = flashcardList[count].shownWord

                ObjectAnimator.ofFloat(shownWord, View.ALPHA, 0f, 1f).apply {
                    duration = 500
                }.start()

                hiddenWordText.text = "Tap here to check"
                if(!endless) countText.text = (count + 1).toString() + "/" + cardCount.toString()
            } else {
                val resultFragment = ResultFragment()
                resultFragment.score = correctAnswers
                if(endless) resultFragment.maxScore = countGuessed
                else resultFragment.maxScore = cardCount
                activity!!.supportFragmentManager.beginTransaction()
                    ?.setCustomAnimations(R.anim.exit_right_to_left, R.anim.enter_right_to_left)
                    .replace(R.id.main_container, resultFragment)
                    .commit()
            }
        } else {
            if(flashcardList[count - 1].hiddenWord == enterText.text.toString()){
                answerIndicator.setImageDrawable(resources.getDrawable(R.drawable.ic_check_circle_24px))
                correctAnswers++
                ObjectAnimator.ofFloat(answerIndicator, View.ALPHA, 0f, 1f).apply {
                    duration = 1000
                }.start()
                answerIndicator.isGone = false
                ObjectAnimator.ofFloat(answerIndicator, View.ALPHA, 1f, 0f).apply {
                    duration = 1000
                }.start()
            } else {
                answerIndicator.setImageDrawable(resources.getDrawable(R.drawable.ic_cancel_24px))
                ObjectAnimator.ofFloat(answerIndicator, View.ALPHA, 0f, 1f).apply {
                    duration = 1000
                }
                answerIndicator.isGone = false
                ObjectAnimator.ofFloat(answerIndicator, View.ALPHA, 1f, 0f).apply {
                    duration = 1000
                }.start()
            }
            Log.e("HIDDEN", flashcardList[count - 1].hiddenWord)
            Log.e("ENTERED", enterText.text.toString())
            Log.e("COUNT", count.toString())
            Log.e("CORRECT", correctAnswers.toString())
            enterText.text.clear()
            if(endless && count == cardCount) {
                count = 0
                val randomValue = kotlin.random.Random.nextLong(1000000, 100000000)
                if(random) flashcardList = flashcardList.shuffled(Random(randomValue))
            }
            if(count < cardCount){

                ObjectAnimator.ofFloat(shownWord, View.ALPHA, 1f, 0f).apply {
                    duration = 500
                }.start()

                shownWord.text = flashcardList[count].shownWord

                ObjectAnimator.ofFloat(shownWord, View.ALPHA, 0f, 1f).apply {
                    duration = 500
                }.start()

                if(!endless) countText.text = (count + 1).toString() + "/" + cardCount.toString()
            } else {
                Log.e("CORRECT", correctAnswers.toString())
                val resultFragment = ResultFragment()
                resultFragment.score = correctAnswers
                if(endless) resultFragment.maxScore = countGuessed
                else resultFragment.maxScore = cardCount
                activity!!.supportFragmentManager.beginTransaction()
                    ?.setCustomAnimations(R.anim.exit_right_to_left, R.anim.enter_right_to_left)
                    .replace(R.id.main_container, resultFragment)
                    .commit()
            }
        }
    }

    private fun onNotCorrectAnswer(){
        count++
        if(endless && count == cardCount) {
            count = 0
            val randomValue = kotlin.random.Random.nextLong(1000000, 100000000)
            if(random) flashcardList = flashcardList.shuffled(Random(randomValue))
        }
        if (count < cardCount) {
            shownWord.text = flashcardList[count].shownWord
            hiddenWordText.text = "Tap here to check"
            if(!endless) countText.text = (count + 1).toString() + "/" + cardCount.toString()
        } else {
            val resultFragment = ResultFragment()
            resultFragment.score = correctAnswers
            if(endless) resultFragment.maxScore = countGuessed
            else resultFragment.maxScore = cardCount
            activity!!.supportFragmentManager.beginTransaction()
                ?.setCustomAnimations(R.anim.exit_right_to_left, R.anim.enter_right_to_left)
                .replace(R.id.main_container, resultFragment)
                .commit()
        }
    }

    private fun onHiddenClick(){
        cardCount = flashcardList.size
        if(typing) {
            //if(random) flashcardList = flashcardList.shuffled(Random(466754))
            ObjectAnimator.ofFloat(shownWord, View.ALPHA, 1f, 0f).apply {
                duration = 500
            }.start()
            shownWord.text = flashcardList[count].shownWord
            ObjectAnimator.ofFloat(shownWord, View.ALPHA, 0f, 1f).apply {
                duration = 500
            }.start()
            if(!endless) countText.text = (count + 1).toString() + "/" + cardCount.toString()

            ObjectAnimator.ofFloat(hiddenWordText, View.ALPHA, 1f, 0f).apply {
                duration = 500
            }.start()
            hiddenWordText.isGone = true

            ObjectAnimator.ofFloat(enterText, View.ALPHA, 0f, 1f).apply {
                duration = 500
            }.start()
            enterText.isGone = false

            ObjectAnimator.ofFloat(notCorrectButton, View.ALPHA, 1f, 0f).apply {
                duration = 500
            }.start()
            notCorrectButton.isGone = true

            ObjectAnimator.ofFloat(correctButton, View.ALPHA, 1f, 0f).apply {
                duration = 500
            }.start()
            correctButton.isGone = true

            ObjectAnimator.ofFloat(typingCorrectButton, View.ALPHA, 0f, 1f).apply {
                duration = 500
            }.start()
            typingCorrectButton.isGone = false
        } else if(!typing){
            //hiddenWordText.text = "Tap here to check"
            if(count < cardCount){
                if(firstHiddenUse){

                    if(endless){
                        ObjectAnimator.ofFloat(endButtonNotTyping, View.ALPHA, 0f, 1f).apply {
                            duration = 500
                        }.start()
                        endButtonNotTyping.isGone = false
                    }

                    ObjectAnimator.ofFloat(correctButton, View.ALPHA, 0f, 1f).apply {
                        duration = 500
                    }.start()
                    correctButton.isGone = false

                    ObjectAnimator.ofFloat(notCorrectButton, View.ALPHA, 0f, 1f).apply {
                        duration = 500
                    }.start()
                    notCorrectButton.isGone = false

                    Log.e("HIDDENCLICKED", "FIRSTTIME")

                    ObjectAnimator.ofFloat(shownWord, View.ALPHA, 1f, 0f).apply {
                        duration = 500
                    }.start()
                    shownWord.text = flashcardList[count].shownWord
                    ObjectAnimator.ofFloat(shownWord, View.ALPHA, 0f, 1f).apply {
                        duration = 500
                    }.start()

                    hiddenWordText.text = "Tap here to check"
                    if(!endless) countText.text = (count + 1).toString() + "/" + cardCount.toString()
                    hiddenClicked = false
                    firstHiddenUse = false
                } else {
                    if (!hiddenClicked) {
                        Log.e("HIDDENCLICKED", "FALSE")

                        shownWord.text = flashcardList[count].shownWord

                        ObjectAnimator.ofFloat(hiddenWordText, View.ALPHA, 1f, 0f).apply {
                            duration = 500
                        }.start()
                        hiddenWordText.text = flashcardList[count].hiddenWord
                        ObjectAnimator.ofFloat(hiddenWordText, View.ALPHA, 0f, 1f).apply {
                            duration = 500
                        }.start()
                        if(!endless) countText.text = (count + 1).toString() + "/" + cardCount.toString()
                        hiddenClicked = true
                    } else {
                        Log.e("HIDDENCLICKED", "TRUE")
                        ObjectAnimator.ofFloat(hiddenWordText, View.ALPHA, 1f, 0f).apply {
                            duration = 500
                        }.start()
                        hiddenWordText.text = "Tap here to check"
                        ObjectAnimator.ofFloat(hiddenWordText, View.ALPHA, 0f, 1f).apply {
                            duration = 500
                        }.start()
                        hiddenClicked = false
                    }
                }
                /*if (!hiddenClicked) {
                    Log.e("HIDDENCLICKED", "FALSE")
                    shownWord.text = flashcardList[count].shownWord
                    if(!endless) countText.text = (count + 1).toString() + "/" + cardCount.toString()
                    hiddenClicked = true
                } else {
                    Log.e("HIDDENCLICKED", "TRUE")
                    hiddenWordText.text = flashcardList[count].hiddenWord
                    hiddenClicked = false
                }*/
            } else {
                Log.e("HIDDENCLICKED", "???")
                hiddenWordText.text = flashcardList[count].hiddenWord
            }
        } else {
            Log.e("QUESTION", "czy tos ie odpala")
            if(count < cardCount){
                shownWord.text = flashcardList[count].shownWord
                if(!endless) countText.text = (count + 1).toString() + "/" + cardCount.toString()
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
