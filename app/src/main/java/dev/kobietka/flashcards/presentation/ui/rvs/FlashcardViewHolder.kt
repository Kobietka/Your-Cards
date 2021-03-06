package dev.kobietka.flashcards.presentation.ui.rvs

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import dev.kobietka.flashcards.R
import dev.kobietka.flashcards.presentation.viewmodel.FlashcardViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class FlashcardViewHolder(itemView: View, val viewModel: FlashcardViewModel) : RecyclerView.ViewHolder(itemView) {

    private val compositeDisposable = CompositeDisposable()

    fun onAttach(){
        compositeDisposable.add(
            viewModel.shownWord
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                if(it.length > 28){
                    val first = it.subSequence(0, 28)
                    val second = "..."
                    itemView.findViewById<TextView>(R.id.text_shown_word_entry).text = "$first$second"
                } else {
                    itemView.findViewById<TextView>(R.id.text_shown_word_entry).text = it
                }
            }
        )

        compositeDisposable.add(
            viewModel.hiddenWord
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    if(it.length > 28){
                        val first = it.subSequence(0, 28)
                        val second = "..."
                        itemView.findViewById<TextView>(R.id.text_hidden_word_entry).text = "$first$second"
                    } else {
                        itemView.findViewById<TextView>(R.id.text_hidden_word_entry).text = it
                    }
                }
        )
    }

    fun onDetach(){
        compositeDisposable.clear()
    }
}