package dev.kobietka.flashcards.presentation.ui.rvs

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.isGone
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import dev.kobietka.flashcards.R
import dev.kobietka.flashcards.data.CardListDao
import dev.kobietka.flashcards.presentation.onclickhandler.OnClickHandler
import dev.kobietka.flashcards.presentation.ui.fragmenteditlist.EditListFragment
import dev.kobietka.flashcards.presentation.viewmodel.ListViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.entry_list.view.*
import javax.inject.Inject
import javax.inject.Provider

class MainAdapter
@Inject constructor(val listDao: CardListDao,
                    private val listViewModelProvider: Provider<ListViewModel>)
    : RecyclerView.Adapter<ListViewHolder>() {

    var idList = listOf<Int>()
    val compositeDisposable = CompositeDisposable()
    val onClickHandler = OnClickHandler()

    private fun updateList(idsList: List<Int>){
        idList = idsList.reversed()
        this.notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.entry_list, parent, false)
        val viewModel = listViewModelProvider.get()
        view.setOnClickListener {
            onClickHandler.changeFocus(it)
        }
        view.findViewById<ImageView>(R.id.settings_icon).setOnClickListener {
            viewModel.toggClick()
        }
        view.findViewById<ImageView>(R.id.play_icon).setOnClickListener {
            viewModel.playClick()
        }
        return ListViewHolder(view, viewModel)
    }

    override fun getItemCount(): Int {
        return idList.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.viewModel.switchIds(idList[position])
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        compositeDisposable.add(
            listDao.getIdsList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::updateList)
        )
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        compositeDisposable.clear()
    }

    override fun onViewAttachedToWindow(holder: ListViewHolder) {
        super.onViewAttachedToWindow(holder)
        holder.onAttach()
    }

    override fun onViewDetachedFromWindow(holder: ListViewHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.onDetach()
    }

}