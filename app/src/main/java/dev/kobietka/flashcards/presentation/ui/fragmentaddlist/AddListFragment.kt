package dev.kobietka.flashcards.presentation.ui.fragmentaddlist

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import dev.kobietka.flashcards.R
import dev.kobietka.flashcards.presentation.ui.common.BaseFragment
import dev.kobietka.flashcards.presentation.ui.fragmentmain.MainFragment
import dev.kobietka.flashcards.presentation.ui.rvs.FlashcardAdapter
import javax.inject.Inject

class AddListFragment : BaseFragment() {

    lateinit var cancelButton: ImageButton
    lateinit var recyclerView: RecyclerView
    @Inject lateinit var adapter: FlashcardAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView


        cancelButton = view.findViewById(R.id.button_cancel_add_list)

        cancelButton.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.main_container, MainFragment())
                ?.addToBackStack(null)
                ?.commit()
        }

    }

    override fun getLayout(): Int {
        return R.layout.fragment_add_list
    }

}