package dev.kobietka.flashcards.presentation.viewmodel

import dev.kobietka.flashcards.data.CardListDao
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.Observable
import javax.inject.Inject

class ListViewModel
@Inject constructor(val listDao: CardListDao){

    private val ids = BehaviorSubject.create<Int>().toSerialized()

    fun switchIds(id: Int){
        ids.onNext(id)
    }

    val listName: Observable<String>
        get() = ids
            .switchMap { id -> listDao.getById(id) }
            .map { it.name }

    val cardCount: Observable<Int>
        get() = ids
            .switchMap { id -> listDao.getById(id) }
            .map { it.count }
}