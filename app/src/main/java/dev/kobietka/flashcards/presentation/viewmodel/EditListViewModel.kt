package dev.kobietka.flashcards.presentation.viewmodel

import dev.kobietka.flashcards.data.CardListDao
import dev.kobietka.flashcards.data.CardListEntity
import dev.kobietka.flashcards.data.FlashcardDao
import dev.kobietka.flashcards.data.FlashcardEntity
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

class EditListViewModel
@Inject constructor(val flashcardDao: FlashcardDao,
                    val listDao: CardListDao){

    var listID = 0
    lateinit var cardList: CardListEntity

    val compositeDisposable = CompositeDisposable()
    private val ids = BehaviorSubject.create<Int>().toSerialized()
    private val card = BehaviorSubject.create<Int>().toSerialized()

    init {
        compositeDisposable.add(
            ids.subscribe { 
                listDao.getById(it)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe { cardListEntity ->
                        setList(cardListEntity)
                        onStart()
                    }
            }
        )
    }

    val listName: Observable<String>
        get() = card.map {
            cardList.name
        }

    val typing: Observable<Boolean>
        get() = card.map {
            cardList.typingAnswer
        }

    val random: Observable<Boolean>
        get() = card.map {
            cardList.randomOrder
        }

    private fun onStart(){
        card.onNext(0)
    }

    fun saveList(listName: String, endless: Boolean, typing: Boolean, random: Boolean){
        updateList(listName, endless, typing, random)
    }

    fun addFlashcard(shownWord: String, hiddenWord: String){
        flashcardDao.insertFlashcard(FlashcardEntity(
            null,
            shownWord,
            hiddenWord,
            listID
        )).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
    }

    private fun updateList(listName: String, endless: Boolean, typing: Boolean, random: Boolean){
        listDao.updateListName(listID, listName)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
        listDao.updateListEndless(listID, endless)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
        listDao.updateListRandom(listID, random)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
        listDao.updateListTyping(listID, typing)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
    }

    fun setID(id: Int){
        listID = id
        ids.onNext(id)
    }

    private fun setList(list: CardListEntity){
        cardList = list
    }
}