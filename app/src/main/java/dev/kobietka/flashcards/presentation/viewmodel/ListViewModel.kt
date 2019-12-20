package dev.kobietka.flashcards.presentation.viewmodel

import android.util.Log
import dev.kobietka.flashcards.data.CardListDao
import dev.kobietka.flashcards.presentation.ui.common.ClickInfo
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.Subject
import javax.inject.Inject
import javax.inject.Named

class ListViewModel
@Inject constructor(val listDao: CardListDao,
                    private val eventsSubject: Subject<ClickInfo>){

    private val ids = BehaviorSubject.create<Int>().toSerialized()
    private val togglClicks = BehaviorSubject.create<Int>().toSerialized()
    private val playClicks = BehaviorSubject.create<Int>().toSerialized()

    init {
        togglClicks.withLatestFrom(ids, BiFunction<Int, Int, Unit> { clickId, listId ->
            eventsSubject.onNext(ClickInfo(listId, clickId ))
            //Log.e("ID", xd.toString())
        }).subscribe()
        playClicks.withLatestFrom(ids, BiFunction<Int, Int, Unit> { clickId, listId ->
            eventsSubject.onNext(ClickInfo(listId, clickId))
            //Log.e("ID", xd.toString())
        }).subscribe()
    }

    fun switchIds(id: Int){
        ids.onNext(id)
    }

    fun playClick(){
        playClicks.onNext(0)
    }

    fun toggClick(){
        togglClicks.onNext(1)
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