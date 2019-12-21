package dev.kobietka.flashcards.presentation.onclickhandler


import android.media.Image
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isGone
import androidx.core.view.isVisible
import dev.kobietka.flashcards.R
import dev.kobietka.flashcards.R.drawable.*


class OnClickHandler {

    var currentView: View? = null
    var firstUse = true

    fun changeFocus(itemView: View){
        if(firstUse) {
            itemView.background = itemView.resources.getDrawable(entry_list_click_background)
            itemView.findViewById<ImageView>(R.id.cards_icon).isGone = true
            itemView.findViewById<TextView>(R.id.count_cards).isGone = true
            itemView.findViewById<ImageView>(R.id.settings_icon).isGone = false
            itemView.findViewById<ImageView>(R.id.play_icon).isGone = false
            currentView = itemView
            firstUse = false
        } else {
            if(itemView == currentView){
                currentView?.background = currentView?.resources?.getDrawable(entry_list_background)
                currentView?.findViewById<ImageView>(R.id.cards_icon)?.isGone = false
                currentView?.findViewById<TextView>(R.id.count_cards)?.isGone = false
                currentView?.findViewById<ImageView>(R.id.settings_icon)?.isGone = true
                currentView?.findViewById<ImageView>(R.id.play_icon)?.isGone = true
                currentView = null
            } else {
                currentView?.background = currentView?.resources?.getDrawable(entry_list_background)
                currentView?.findViewById<ImageView>(R.id.cards_icon)?.isGone = false
                currentView?.findViewById<TextView>(R.id.count_cards)?.isGone = false
                currentView?.findViewById<ImageView>(R.id.settings_icon)?.isGone = true
                currentView?.findViewById<ImageView>(R.id.play_icon)?.isGone = true
                itemView.background = itemView.resources.getDrawable(entry_list_click_background)
                itemView.findViewById<ImageView>(R.id.settings_icon).isGone = false
                itemView.findViewById<ImageView>(R.id.play_icon).isGone = false
                itemView.findViewById<ImageView>(R.id.cards_icon).isGone = true
                itemView.findViewById<TextView>(R.id.count_cards).isGone = true
                currentView = itemView
            }
        }
    }

    fun changeFocusFlashcard(itemView: View){
        if(firstUse) {
            itemView.background = itemView.resources.getDrawable(entry_list_click_background)
            itemView.findViewById<ImageView>(R.id.entry_flashcard_delete).isGone = false
            currentView = itemView
            firstUse = false
        } else {
            if(itemView == currentView){
                currentView?.background = currentView?.resources?.getDrawable(entry_list_background)
                currentView?.findViewById<ImageView>(R.id.entry_flashcard_delete)?.isGone = true
                currentView = null
            } else {
                currentView?.background = currentView?.resources?.getDrawable(entry_list_background)
                currentView?.findViewById<ImageView>(R.id.entry_flashcard_delete)?.isGone = true
                itemView.background = itemView.resources.getDrawable(entry_list_click_background)
                itemView.findViewById<ImageView>(R.id.entry_flashcard_delete).isGone = false
                currentView = itemView
            }
        }
    }
}