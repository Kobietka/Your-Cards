package dev.kobietka.flashcards.presentation.ui.fragmentmain

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dev.kobietka.flashcards.R
import dev.kobietka.flashcards.data.CardListDao
import dev.kobietka.flashcards.data.CardListEntity
import dev.kobietka.flashcards.presentation.ui.common.BaseFragment
import dev.kobietka.flashcards.presentation.ui.fragmentaddlist.AddListFragment
import dev.kobietka.flashcards.presentation.ui.rvs.MainAdapter
import javax.inject.Inject

class MainFragment: BaseFragment() {

    lateinit var recyclerView: RecyclerView
    @Inject lateinit var adapter: MainAdapter
    lateinit var addButton: Button

    @Inject lateinit var listDao: CardListDao

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presentationComponent.inject(this)

        recyclerView = view.findViewById(R.id.rv_main)
        recyclerView.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        recyclerView.adapter = adapter

        addButton = view.findViewById(R.id.button_add)
        addButton.setOnClickListener {
            activity?.supportFragmentManager
                ?.beginTransaction()
                ?.replace(R.id.main_container, AddListFragment())
                ?.addToBackStack(null)
                ?.commit()
        }
    }

    override fun getLayout(): Int {
        return R.layout.fragment_main
    }

}