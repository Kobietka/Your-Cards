package dev.kobietka.flashcards.presentation.onclickhandler


import android.animation.ObjectAnimator
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isGone
import dev.kobietka.flashcards.R
import dev.kobietka.flashcards.R.drawable.*


class OnClickHandler {

    var currentView: View? = null
    var firstUse = true


    //Clearing the view
    fun setViewToNull(){
        currentView?.background = currentView?.resources?.getDrawable(entry_list_background)
        currentView?.findViewById<ImageView>(R.id.entry_flashcard_delete)?.isGone = true
        currentView = null
    }

    //Handling the views properties when user clicks on it
    fun changeFocus(itemView: View){
        if(firstUse) {

            itemView.background = itemView.resources.getDrawable(entry_list_click_background)

            animateHide(itemView.findViewById<ImageView>(R.id.cards_icon))
            itemView.findViewById<ImageView>(R.id.cards_icon).isGone = true

            animateHide(itemView.findViewById<TextView>(R.id.count_cards))
            itemView.findViewById<TextView>(R.id.count_cards).isGone = true

            animateShow(itemView.findViewById<ImageView>(R.id.settings_icon))
            itemView.findViewById<ImageView>(R.id.settings_icon).isGone = false

            animateShow(itemView.findViewById<ImageView>(R.id.play_icon))
            itemView.findViewById<ImageView>(R.id.play_icon).isGone = false

            currentView = itemView
            firstUse = false

        } else {
            if(itemView == currentView){
                currentView?.background = currentView?.resources?.getDrawable(entry_list_background)

                animateShow(currentView?.findViewById<ImageView>(R.id.cards_icon))
                currentView?.findViewById<ImageView>(R.id.cards_icon)?.isGone = false

                animateShow(currentView?.findViewById<TextView>(R.id.count_cards))
                currentView?.findViewById<TextView>(R.id.count_cards)?.isGone = false

                animateHide(currentView?.findViewById<ImageView>(R.id.settings_icon))
                currentView?.findViewById<ImageView>(R.id.settings_icon)?.isGone = true

                animateHide(currentView?.findViewById<ImageView>(R.id.play_icon))
                currentView?.findViewById<ImageView>(R.id.play_icon)?.isGone = true
                currentView = null
            } else {
                currentView?.background = currentView?.resources?.getDrawable(entry_list_background)

                animateShow(currentView?.findViewById<ImageView>(R.id.cards_icon))
                currentView?.findViewById<ImageView>(R.id.cards_icon)?.isGone = false

                animateShow(currentView?.findViewById<TextView>(R.id.count_cards))
                currentView?.findViewById<TextView>(R.id.count_cards)?.isGone = false

                animateHide(currentView?.findViewById<ImageView>(R.id.settings_icon))
                currentView?.findViewById<ImageView>(R.id.settings_icon)?.isGone = true

                animateHide(currentView?.findViewById<ImageView>(R.id.play_icon))
                currentView?.findViewById<ImageView>(R.id.play_icon)?.isGone = true

                itemView.background = itemView.resources.getDrawable(entry_list_click_background)

                animateShow(itemView.findViewById<ImageView>(R.id.settings_icon))
                itemView.findViewById<ImageView>(R.id.settings_icon).isGone = false

                animateShow(itemView.findViewById<ImageView>(R.id.play_icon))
                itemView.findViewById<ImageView>(R.id.play_icon).isGone = false

                animateShow(itemView.findViewById<ImageView>(R.id.cards_icon))
                itemView.findViewById<ImageView>(R.id.cards_icon).isGone = true

                animateShow(itemView.findViewById<TextView>(R.id.count_cards))
                itemView.findViewById<TextView>(R.id.count_cards).isGone = true
                currentView = itemView
            }
        }
    }

    //Handling the views properties when user clicks on it
    fun changeFocusFlashcard(itemView: View){
        if(firstUse) {
            itemView.background = itemView.resources.getDrawable(entry_list_click_background)

            animateShow(itemView.findViewById<ImageView>(R.id.entry_flashcard_delete))
            itemView.findViewById<ImageView>(R.id.entry_flashcard_delete).isGone = false

            currentView = itemView
            firstUse = false
        } else {
            if(itemView == currentView){
                currentView?.background = currentView?.resources?.getDrawable(entry_list_background)

                animateHide(currentView?.findViewById<ImageView>(R.id.entry_flashcard_delete))
                currentView?.findViewById<ImageView>(R.id.entry_flashcard_delete)?.isGone = true

                currentView = null

            } else {
                currentView?.background = currentView?.resources?.getDrawable(entry_list_background)

                animateHide(currentView?.findViewById<ImageView>(R.id.entry_flashcard_delete))
                currentView?.findViewById<ImageView>(R.id.entry_flashcard_delete)?.isGone = true

                itemView.background = itemView.resources.getDrawable(entry_list_click_background)

                animateShow(itemView.findViewById<ImageView>(R.id.entry_flashcard_delete))
                itemView.findViewById<ImageView>(R.id.entry_flashcard_delete).isGone = false

                currentView = itemView
            }
        }
    }

    private fun animateShow(view: View?){
        if(view == null) return
        ObjectAnimator.ofFloat(view, View.ALPHA, 0f, 1f).apply {
            duration = 500
        }.start()
    }

    private fun animateHide(view: View?){
        if(view == null) return
        ObjectAnimator.ofFloat(view, View.ALPHA, 1f, 0f).apply {
            duration = 500
        }.start()
    }
}