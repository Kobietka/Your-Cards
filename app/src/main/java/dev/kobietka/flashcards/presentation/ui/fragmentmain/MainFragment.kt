package dev.kobietka.flashcards.presentation.ui.fragmentmain

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import com.mikepenz.aboutlibraries.*
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import dev.kobietka.flashcards.R
import dev.kobietka.flashcards.data.CardListDao
import dev.kobietka.flashcards.data.FlashcardDao
import dev.kobietka.flashcards.presentation.swipetodeletecallback.SwipeToDeleteCallback
import dev.kobietka.flashcards.presentation.ui.common.BaseFragment
import dev.kobietka.flashcards.presentation.ui.fragmentaddlist.AddListFragment
import dev.kobietka.flashcards.presentation.ui.rvs.ListViewHolder
import dev.kobietka.flashcards.presentation.ui.rvs.MainAdapter
import javax.inject.Inject

class MainFragment: BaseFragment() {

    lateinit var recyclerView: RecyclerView
    lateinit var addButton: Button
    lateinit var infoButton: ImageView

    @Inject lateinit var listDao: CardListDao
    @Inject lateinit var adapter: MainAdapter
    @Inject lateinit var flashcardDao: FlashcardDao

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presentationComponent.inject(this)

        recyclerView = view.findViewById(R.id.rv_main)
        recyclerView.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false) as RecyclerView.LayoutManager?
        recyclerView.adapter = adapter

        @SuppressLint("CheckResult")
        val swipeHandler = object : SwipeToDeleteCallback(activity!!){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                (viewHolder as ListViewHolder).viewModel.deleteOnSwipe()
                val snackbar = Snackbar.make( view, "List has been deleted!", Snackbar.LENGTH_LONG)
                snackbar.setAction("Undo", View.OnClickListener {
                    (viewHolder as ListViewHolder).viewModel.reCreateList()
                })
                snackbar.show()
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(recyclerView)

        addButton = view.findViewById(R.id.button_add)
        infoButton = view.findViewById(R.id.button_info)

        addButton.setOnClickListener {
            activity?.supportFragmentManager
                ?.beginTransaction()
                ?.setCustomAnimations(R.anim.exit_right_to_left, R.anim.enter_right_to_left)
                ?.replace(R.id.main_container, AddListFragment())
                ?.addToBackStack(null)
                ?.commit()
        }

        infoButton.setOnClickListener {
            LibsBuilder()
                .withActivityStyle(Libs.ActivityStyle.LIGHT_DARK_TOOLBAR)
                .start(activity!!)
        }
    }


    override fun getLayout(): Int {
        return R.layout.fragment_main
    }

}