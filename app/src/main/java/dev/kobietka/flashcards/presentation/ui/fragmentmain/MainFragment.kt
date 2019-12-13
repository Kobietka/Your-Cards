package dev.kobietka.flashcards.presentation.ui.fragmentmain

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dev.kobietka.flashcards.R
import dev.kobietka.flashcards.presentation.ui.common.BaseFragment
import dev.kobietka.flashcards.presentation.ui.rvs.MainAdapter
import javax.inject.Inject

class MainFragment: BaseFragment() {

    lateinit var recyclerView: RecyclerView
    @Inject lateinit var adapter: MainAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presentationComponent.inject(this)

        recyclerView = view.findViewById(R.id.rv_main)
        recyclerView.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        recyclerView.adapter = adapter
    }

    override fun getLayout(): Int {
        return R.layout.fragment_main
    }

}