package dev.kobietka.flashcards.presentation.ui.rvs

import android.content.Context
import android.media.Image
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import dev.kobietka.flashcards.R
import dev.kobietka.flashcards.data.FlashcardDao
import dev.kobietka.flashcards.data.FlashcardEntity
import dev.kobietka.flashcards.presentation.onclickhandler.OnClickHandler
import dev.kobietka.flashcards.presentation.viewmodel.FlashcardViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Provider

class FlashcardEditAdapter
@Inject constructor(
    private val viewModelProvider: Provider<FlashcardViewModel>,
    val flashcardDao: FlashcardDao
): RecyclerView.Adapter<FlashcardViewHolder>() {

    private var idsList: List<Int?> = listOf()
    private val compositeDisposable = CompositeDisposable()
    var listId = 0
    val clickHandler = OnClickHandler()

    private fun updateIds(newList: List<FlashcardEntity>){
        val goodIds = newList.filter {
            it.listId == listId
        }.map { it.id }
        idsList = goodIds.reversed()
        this.notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlashcardViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.entry_flashcard, parent, false)
        val viewModel = viewModelProvider.get()
        view.setOnClickListener {
            clickHandler.changeFocusFlashcard(it)
        }
        view.findViewById<ImageView>(R.id.entry_flashcard_delete).setOnClickListener {
            viewModel.deleteClick()
            clickHandler.setViewToNull()
        }
        return FlashcardViewHolder(view, viewModel)
    }

    override fun getItemCount(): Int {
        return idsList.size
    }

    override fun onBindViewHolder(holder: FlashcardViewHolder, position: Int) {
        holder.viewModel.switchId(idsList[position])
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        compositeDisposable.add(
            flashcardDao.getAllCards()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::updateIds)
        )
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        compositeDisposable.clear()
    }

    override fun onViewAttachedToWindow(holder: FlashcardViewHolder) {
        super.onViewAttachedToWindow(holder)
        holder.onAttach()
    }

    override fun onViewDetachedFromWindow(holder: FlashcardViewHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.onDetach()
    }

    fun setCardListId(id: Int){
        listId = id
    }

}