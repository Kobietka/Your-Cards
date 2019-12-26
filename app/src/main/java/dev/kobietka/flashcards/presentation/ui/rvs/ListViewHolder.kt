package dev.kobietka.flashcards.presentation.ui.rvs

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import dev.kobietka.flashcards.R
import dev.kobietka.flashcards.presentation.viewmodel.ListViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class ListViewHolder(itemView: View, val viewModel: ListViewModel) : RecyclerView.ViewHolder(itemView) {

    private val compositeDisposable = CompositeDisposable()

    fun onAttach(){
        compositeDisposable.add(
            viewModel.listName
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    if(it.length > 21){
                        val first = it.subSequence(0, 21)
                        val second = "..."
                        itemView.findViewById<TextView>(R.id.name_list).text = "$first$second"
                    } else {
                        itemView.findViewById<TextView>(R.id.name_list).text = it
                    }
                }
        )

        compositeDisposable.add(
            viewModel.cardCount
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    itemView.findViewById<TextView>(R.id.count_cards).text = it.toString()
                }
        )
    }

    fun onDetach(){
        compositeDisposable.clear()
    }
}