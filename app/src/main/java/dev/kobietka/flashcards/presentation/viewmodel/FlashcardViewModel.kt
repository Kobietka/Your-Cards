package dev.kobietka.flashcards.presentation.viewmodel

import io.reactivex.Observable
import dev.kobietka.flashcards.data.FlashcardDao
import io.reactivex.Completable
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

class FlashcardViewModel
@Inject constructor(val flashcardDao: FlashcardDao){

    private val ids = BehaviorSubject.create<Int>().toSerialized()
    private val deleteClicks = BehaviorSubject.create<Int>().toSerialized()

    init {
        deleteClicks.withLatestFrom(ids, BiFunction<Int, Int, Completable>{ clickId, flashcardId ->
            flashcardDao.deleteById(flashcardId)
        }).flatMapCompletable { it }.subscribe()
    }

    fun switchId(id: Int?){
        ids.onNext(id!!)
    }

    fun deleteClick(){
        deleteClicks.onNext(2)
    }

    val hiddenWord: Observable<String>
        get() = ids
            .switchMap { id -> flashcardDao.getById(id) }
            .map { it.hiddenWord }

    val shownWord: Observable<String>
        get() = ids
            .switchMap { id -> flashcardDao.getById(id) }
            .map { it.shownWord }

}


