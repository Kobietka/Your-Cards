package dev.kobietka.flashcards.presentation.viewmodel

import io.reactivex.Observable
import dev.kobietka.flashcards.data.FlashcardDao
import dev.kobietka.flashcards.presentation.ui.common.ClickInfo
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.Subject
import javax.inject.Inject

class FlashcardViewModel
@Inject constructor(val flashcardDao: FlashcardDao,
                    private val eventsSubject: Subject<ClickInfo>,
                    private val deleteSubject: Subject<Int>){

    private val ids = BehaviorSubject.create<Int>().toSerialized()
    private val deleteClicks = BehaviorSubject.create<Int>().toSerialized()

    init {
        deleteClicks.withLatestFrom(ids, BiFunction<Int, Int, Unit>{ clickId, flashcardId ->
            deleteSubject.onNext(flashcardId)
        }).subscribe()
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


