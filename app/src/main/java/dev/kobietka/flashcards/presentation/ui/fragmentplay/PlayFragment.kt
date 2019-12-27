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
import dev.kobietka.flashcards.presentation.viewmodel.PlayViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import kotlin.random.Random

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
    private lateinit var playNotCorrectNotTypingNotEndless: RelativeLayout
    private lateinit var playCorrectNotTypingNotEndless: RelativeLayout
    private lateinit var playCorrectTypingOnly: RelativeLayout
    private lateinit var hiddenWordTextTap: TextView
    private lateinit var startButton: RelativeLayout

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

    val resultFragment = ResultFragment()

    @Inject lateinit var flashcardDao: FlashcardDao
    @Inject lateinit var listDao: CardListDao
    @Inject lateinit var events: Observable<ClickInfo>
    @Inject lateinit var viewModel: PlayViewModel
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
        playNotCorrectNotTypingNotEndless = view.findViewById(R.id.play_not_correct_not_typing_not_endless)
        playCorrectNotTypingNotEndless = view.findViewById(R.id.play_correct_typing_not_endless)
        playCorrectTypingOnly = view.findViewById(R.id.play_correct_typing_only)
        hiddenWordTextTap = view.findViewById(R.id.play_hidden_word_text_tap)
        startButton = view.findViewById(R.id.play_start_button)


        compositeDisposable.add(
            events.map {
                it.listId
            }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(viewModel::setListID)
        )

        compositeDisposable.add(
            viewModel.resultValue.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    resultFragment.ratio = it
                }
        )

        compositeDisposable.add(
            viewModel.isTyping.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    if(it){
                        playCorrectTypingOnly.isGone = false
                        enterText.isGone = false
                        hiddenWordText.isGone = true
                    } else {
                        playCorrectNotTypingNotEndless.isGone = false
                        playNotCorrectNotTypingNotEndless.isGone = false
                        hiddenWordText.isGone = true
                        hiddenWordTextTap.text = "Tap here to check"
                    }
                }
        )

        compositeDisposable.add(
            viewModel.isAnswerCorrect.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    if(it){
                        answerIndicator.setImageDrawable(resources.getDrawable(R.drawable.ic_check_circle_24px))
                        animateShow(answerIndicator)
                        answerIndicator.isGone = false
                        animateHide(answerIndicator)
                    } else {
                        answerIndicator.setImageDrawable(resources.getDrawable(R.drawable.ic_cancel_24px))
                        animateShow(answerIndicator)
                        answerIndicator.isGone = false
                        animateHide(answerIndicator)
                    }
                }
        )

        compositeDisposable.add(
            viewModel.runnable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    if(it){
                        val snackbar: Snackbar = Snackbar.make(view,
                            "Cannot start because the list is empty!",
                            Snackbar.LENGTH_LONG)
                        snackbar.show()
                        activity!!.supportFragmentManager.beginTransaction()
                            .setCustomAnimations(R.anim.exit_right_to_left, R.anim.enter_right_to_left)
                            .replace(R.id.main_container, MainFragment())
                            .commit()
                    }
                }
        )

        compositeDisposable.add(
            viewModel.shownWord.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    shownWord.text = it
                }
        )

        compositeDisposable.add(
            viewModel.hiddenWord.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete {
                    hiddenWordTextTap.isGone = true
                    activity!!.supportFragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.exit_right_to_left, R.anim.enter_right_to_left)
                        .replace(R.id.main_container, resultFragment)
                        .commit()
                }
                .subscribe {
                    hiddenWordText.text = it
                }
        )

        compositeDisposable.add(
            viewModel.count.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    countText.text = it.toString()
                }
        )

        compositeDisposable.add(
            viewModel.listName.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    listName.text = it
                }
        )


        startButton.setOnClickListener {
            viewModel.onStart()
            startButton.isGone = true
        }

        correctButton.setOnClickListener {

        }

        notCorrectButton.setOnClickListener {

        }

        typingCorrectButton.setOnClickListener {

        }

        playNotCorrectNotTypingNotEndless.setOnClickListener {
            hiddenWordText.isGone = true
            hiddenWordTextTap.isGone = false
            viewModel.onNotCorrectClick()
        }

        playCorrectNotTypingNotEndless.setOnClickListener {
            hiddenWordText.isGone = true
            hiddenWordTextTap.isGone = false
            viewModel.onCorrectClick()
        }

        playCorrectTypingOnly.setOnClickListener {
            viewModel.checkAnswer(enterText.text.toString())
            enterText.text.clear()
        }

        hiddenWord.setOnClickListener {
            hiddenWordTextTap.isGone = true
            hiddenWordText.isGone = false
        }

        endButtonNotTyping.setOnClickListener {
            val resultFragment = ResultFragment()
            resultFragment.score = correctAnswers
            if(endless) resultFragment.maxScore = countGuessed
            else resultFragment.maxScore = cardCount
            activity!!.supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.exit_right_to_left, R.anim.enter_right_to_left)
                .replace(R.id.main_container, resultFragment)
                .commit()
        }

        endButton.setOnClickListener {
            val resultFragment = ResultFragment()
            resultFragment.score = correctAnswers
            if(endless) resultFragment.maxScore = countGuessed
            else resultFragment.maxScore = cardCount
            activity!!.supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.exit_right_to_left, R.anim.enter_right_to_left)
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

                animateHide(shownWord)
                shownWord.text = flashcardList[count].shownWord

                animateShow(shownWord)
                hiddenWordText.text = "Tap here to check"
                if(!endless) countText.text = (count + 1).toString() + "/" + cardCount.toString()
            } else {
                val resultFragment = ResultFragment()
                resultFragment.score = correctAnswers
                if(endless) resultFragment.maxScore = countGuessed
                else resultFragment.maxScore = cardCount
                activity!!.supportFragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.exit_right_to_left, R.anim.enter_right_to_left)
                    .replace(R.id.main_container, resultFragment)
                    .commit()
            }
        } else {
            if(flashcardList[count - 1].hiddenWord == enterText.text.toString()){
                answerIndicator.setImageDrawable(resources.getDrawable(R.drawable.ic_check_circle_24px))
                //correctAnswers++

                animateShow(answerIndicator)
                answerIndicator.isGone = false
                animateHide(answerIndicator)
            } else {
                answerIndicator.setImageDrawable(resources.getDrawable(R.drawable.ic_cancel_24px))
                animateShow(answerIndicator)
                answerIndicator.isGone = false
                animateHide(answerIndicator)
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

                animateHide(shownWord)
                shownWord.text = flashcardList[count].shownWord
                animateShow(shownWord)

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

            animateHide(shownWord)
            shownWord.text = flashcardList[count].shownWord
            animateShow(shownWord)
            animateHide(hiddenWordText)
            hiddenWordText.text = "Tap here to check"
            animateShow(hiddenWordText)

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

            animateHide(shownWord)
            shownWord.text = flashcardList[count].shownWord
            animateShow(shownWord)
            if(!endless) countText.text = (count + 1).toString() + "/" + cardCount.toString()

            animateHide(hiddenWordText)
            hiddenWordText.isGone = true

            animateShow(enterText)
            enterText.isGone = false

            if(endless){
                animateShow(endButton)
                endButton.isGone = false
                animateShow(typingCorrectButton)
                typingCorrectButton.isGone = false
            } else {
                animateShow(playCorrectTypingOnly)
                playCorrectTypingOnly.isGone = false
            }

            animateHide(notCorrectButton)
            notCorrectButton.isGone = true

            animateHide(correctButton)
            correctButton.isGone = true

        } else if(!typing){
            if(count < cardCount){
                if(firstHiddenUse){

                    if(endless){
                        animateShow(endButtonNotTyping)
                        endButtonNotTyping.isGone = false

                        animateShow(correctButton)
                        correctButton.isGone = false

                        animateShow(notCorrectButton)
                        notCorrectButton.isGone = false
                    } else {
                        animateShow(playNotCorrectNotTypingNotEndless)
                        playNotCorrectNotTypingNotEndless.isGone = false

                        animateShow(playCorrectNotTypingNotEndless)
                        playCorrectNotTypingNotEndless.isGone = false
                    }


                    Log.e("HIDDENCLICKED", "FIRSTTIME")

                    animateHide(shownWord)
                    shownWord.text = flashcardList[count].shownWord
                    animateShow(shownWord)

                    hiddenWordText.text = "Tap here to check"
                    if(!endless) countText.text = (count + 1).toString() + "/" + cardCount.toString()
                    hiddenClicked = false
                    firstHiddenUse = false
                } else {
                    if (!hiddenClicked) {
                        Log.e("HIDDENCLICKED", "FALSE")

                        shownWord.text = flashcardList[count].shownWord

                        animateHide(hiddenWordText)
                        hiddenWordText.text = flashcardList[count].hiddenWord
                        animateShow(hiddenWordText)

                        if(!endless) countText.text = (count + 1).toString() + "/" + cardCount.toString()
                        hiddenClicked = true
                    } else {
                        Log.e("HIDDENCLICKED", "TRUE")
                        animateHide(hiddenWordText)
                        hiddenWordText.text = "Tap here to check"
                        animateShow(hiddenWordText)
                        hiddenClicked = false
                    }
                }
            } else {
                Log.e("HIDDENCLICKED", "???")
                hiddenWordText.text = flashcardList[count].hiddenWord
            }
        }
    }

    private fun setNewFlashcardList(list: List<FlashcardEntity>){
        flashcardList = list
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

    override fun getLayout(): Int {
        return R.layout.fragment_play
    }

}
