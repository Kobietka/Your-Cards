package dev.kobietka.flashcards.presentation.viewmodel

import dev.kobietka.flashcards.data.CardListDao
import dev.kobietka.flashcards.data.CardListEntity
import dev.kobietka.flashcards.data.FlashcardDao
import dev.kobietka.flashcards.data.FlashcardEntity
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

class AddListViewModel
@Inject constructor(val flashcardDao: FlashcardDao,
                    val listDao: CardListDao){

    val id = BehaviorSubject.create<Int>().toSerialized()

    var listID: Long = 0
    var flashcardCount = 0

    fun createList(listName: String, endless: Boolean, typing: Boolean, random: Boolean){
        listID = listDao.insertList(CardListEntity(
            id = null,
            name = listName,
            endless = endless,
            typingAnswer = typing,
            randomOrder = random
        ))
        id.onNext(listID.toInt())
    }

    fun saveList(listName: String, endless: Boolean, typing: Boolean, random: Boolean){
        updateList(listName, endless, typing, random)
    }

    fun addFlashcard(shownWord: String, hiddenWord: String){
        flashcardDao.insertFlashcard(FlashcardEntity(
            null,
            shownWord,
            hiddenWord,
            listID.toInt()
        )).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
    }

    private fun updateList(listName: String, endless: Boolean, typing: Boolean, random: Boolean){
        listDao.updateListName(listID.toInt(), listName)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
        listDao.updateListEndless(listID.toInt(), endless)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
        listDao.updateListRandom(listID.toInt(), random)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
        listDao.updateListTyping(listID.toInt(), typing)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
    }

    val listId: Observable<Int>
        get() = id

}