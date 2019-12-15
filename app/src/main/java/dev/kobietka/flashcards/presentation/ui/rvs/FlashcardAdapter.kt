package dev.kobietka.flashcards.presentation.ui.rvs

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.kobietka.flashcards.R
import dev.kobietka.flashcards.data.FlashcardDao
import dev.kobietka.flashcards.presentation.viewmodel.FlashcardViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Provider

class FlashcardAdapter
    @Inject constructor(
        @Named("ActivityContext") val activity: Context,
        val viewModelProvider: Provider<FlashcardViewModel>,
        val flashcardDao: FlashcardDao
    ): RecyclerView.Adapter<FlashcardViewHolder>() {

    private var idsList: List<Int> = listOf()
    private val compositeDisposable = CompositeDisposable()

    private fun updateIds(newList: List<Int>){
        idsList = newList
        this.notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlashcardViewHolder {
        val inflater = LayoutInflater.from(activity)
        val view = inflater.inflate(R.layout.entry_flashcard, parent, false)
        return FlashcardViewHolder(view, viewModelProvider.get())
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
            flashcardDao.getAllIds()
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

}