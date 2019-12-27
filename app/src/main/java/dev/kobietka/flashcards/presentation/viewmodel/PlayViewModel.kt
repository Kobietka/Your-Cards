package dev.kobietka.flashcards.presentation.viewmodel

import dev.kobietka.flashcards.data.CardListDao
import dev.kobietka.flashcards.data.CardListEntity
import dev.kobietka.flashcards.data.FlashcardDao
import dev.kobietka.flashcards.data.FlashcardEntity
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

class PlayViewModel
@Inject constructor(val flashcardDao: FlashcardDao,
                    val listDao: CardListDao){

    private val compositeDisposable = CompositeDisposable()
    private lateinit var cardListEntity: CardListEntity
    private lateinit var flashcardList: List<FlashcardEntity>

    private val ids = BehaviorSubject.create<Int>().toSerialized()
    private val cards = BehaviorSubject.create<Int>().toSerialized()
    private val stop = BehaviorSubject.create<Boolean>().toSerialized()
    private val result = BehaviorSubject.create<Double>().toSerialized()

    var currentPosition = 0
    var correctAnswers = 0

    init {
        compositeDisposable.add(
            ids.subscribe { id ->
                flashcardDao.getAllFlashcardsByListId(id).subscribe(this::setFlashcardsList)
                listDao.getById(id).subscribe(this::setCardList)
            }
        )
    }

    fun setListID(id: Int){
        ids.onNext(id)
    }

    fun onStart(){
        if(flashcardList.isNotEmpty()){
            stop.onNext(true)
            cards.onNext(currentPosition)
            currentPosition++
        } else stop.onNext(false)
    }

    fun onCorrectClick(){
        if(currentPosition < flashcardList.size){
            cards.onNext(currentPosition)
            currentPosition++
            correctAnswers++
        } else {
            correctAnswers++
            result.onNext(0.1)
            cards.onComplete()
        }
    }

    fun onNotCorrectClick(){
        if(currentPosition < flashcardList.size){
            cards.onNext(currentPosition)
            currentPosition++
        } else {
            result.onNext(0.1)
            cards.onComplete()
        }
    }

    val shownWord: Observable<String>
        get() = cards.map { position ->
            flashcardList[position].shownWord
        }

    val hiddenWord: Observable<String>
        get() = cards.map { position ->
            flashcardList[position].hiddenWord
        }

    val count: Observable<Int>
        get() = cards.map {
            it + 1
        }

    val listName: Observable<String>
        get() = cards.map {
            cardListEntity.name
        }

    val runnable: Observable<Boolean>
        get() = stop.map {
            it
        }

    val resultValue: Observable<Double>
        get() = result.map {
            correctAnswers.toDouble()/flashcardList.size
        }

    private fun setCardList(cardList: CardListEntity){
        cardListEntity = cardList
    }

    private fun setFlashcardsList(flashcardsList: List<FlashcardEntity>){
        flashcardList = flashcardsList
    }

}