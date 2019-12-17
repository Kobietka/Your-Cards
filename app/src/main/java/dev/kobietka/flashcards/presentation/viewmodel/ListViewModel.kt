package dev.kobietka.flashcards.presentation.viewmodel

import dev.kobietka.flashcards.data.CardListDao
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.Subject
import javax.inject.Inject

class ListViewModel
@Inject constructor(val listDao: CardListDao,
                    private val launchEventsSubject: Subject<Int>){

    private val ids = BehaviorSubject.create<Int>().toSerialized()
    private val clicks = BehaviorSubject.create<Int>().toSerialized()

    init {
        clicks.withLatestFrom(ids, BiFunction<Int, Int, Unit> { _, id ->
            launchEventsSubject.onNext(id)
        }).subscribe()
    }

    fun switchIds(id: Int){
        ids.onNext(id)
    }

    fun toggClick(){
        clicks.onNext(1)
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