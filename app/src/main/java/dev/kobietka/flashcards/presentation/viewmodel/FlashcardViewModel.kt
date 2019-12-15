package dev.kobietka.flashcards.presentation.viewmodel

import io.reactivex.Observable
import dev.kobietka.flashcards.data.FlashcardDao
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

class FlashcardViewModel
@Inject constructor(val flashcardDao: FlashcardDao){

    private val ids = BehaviorSubject.create<Int>().toSerialized()

    fun switchId(id: Int?){
        ids.onNext(id!!)
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


