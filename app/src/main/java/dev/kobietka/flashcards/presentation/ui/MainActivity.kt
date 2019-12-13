package dev.kobietka.flashcards

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dev.kobietka.flashcards.presentation.ui.common.BaseActivity
import dev.kobietka.flashcards.presentation.ui.rvs.MainAdapter
import javax.inject.Inject

class MainActivity : BaseActivity() {

    //lateinit var recyclerView: RecyclerView
    //@Inject lateinit var adapter: MainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        /*presentationComponent.inject(this)
        recyclerView = findViewById(R.id.rv_main)
        recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recyclerView.adapter = adapter*/
    }

}
