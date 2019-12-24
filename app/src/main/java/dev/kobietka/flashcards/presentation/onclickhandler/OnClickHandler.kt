package dev.kobietka.flashcards.presentation.onclickhandler


import android.animation.ObjectAnimator
import android.graphics.drawable.TransitionDrawable
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

    fun setViewToNull(){
        currentView?.background = currentView?.resources?.getDrawable(entry_list_background)
        currentView?.findViewById<ImageView>(R.id.entry_flashcard_delete)?.isGone = true
        currentView = null
    }

    fun changeFocus(itemView: View){
        if(firstUse) {

            itemView.background = itemView.resources.getDrawable(entry_list_click_background)

            ObjectAnimator.ofFloat(itemView.findViewById<ImageView>(R.id.cards_icon), View.ALPHA, 1f, 0f).apply {
                duration = 500
            }.start()
            itemView.findViewById<ImageView>(R.id.cards_icon).isGone = true

            ObjectAnimator.ofFloat(itemView.findViewById<ImageView>(R.id.count_cards), View.ALPHA, 1f, 0f).apply {
                duration = 500
            }.start()
            itemView.findViewById<TextView>(R.id.count_cards).isGone = true

            ObjectAnimator.ofFloat(itemView.findViewById<ImageView>(R.id.settings_icon), View.ALPHA, 0f, 1f).apply {
                duration = 500
            }.start()
            itemView.findViewById<ImageView>(R.id.settings_icon).isGone = false

            ObjectAnimator.ofFloat(itemView.findViewById<ImageView>(R.id.play_icon), View.ALPHA, 0f, 1f).apply {
                duration = 500
            }.start()
            itemView.findViewById<ImageView>(R.id.play_icon).isGone = false

            currentView = itemView
            firstUse = false
        } else {
            if(itemView == currentView){
                currentView?.background = currentView?.resources?.getDrawable(entry_list_background)

                ObjectAnimator.ofFloat(itemView.findViewById<ImageView>(R.id.cards_icon), View.ALPHA, 0f, 1f).apply {
                    duration = 500
                }.start()
                currentView?.findViewById<ImageView>(R.id.cards_icon)?.isGone = false

                ObjectAnimator.ofFloat(itemView.findViewById<ImageView>(R.id.count_cards), View.ALPHA, 0f, 1f).apply {
                    duration = 500
                }.start()
                currentView?.findViewById<TextView>(R.id.count_cards)?.isGone = false

                ObjectAnimator.ofFloat(itemView.findViewById<ImageView>(R.id.settings_icon), View.ALPHA, 1f, 0f).apply {
                    duration = 500
                }.start()
                currentView?.findViewById<ImageView>(R.id.settings_icon)?.isGone = true

                ObjectAnimator.ofFloat(itemView.findViewById<ImageView>(R.id.play_icon), View.ALPHA, 1f, 0f).apply {
                    duration = 500
                }.start()
                currentView?.findViewById<ImageView>(R.id.play_icon)?.isGone = true
                currentView = null
            } else {
                currentView?.background = currentView?.resources?.getDrawable(entry_list_background)

                ObjectAnimator.ofFloat(currentView?.findViewById<ImageView>(R.id.cards_icon), View.ALPHA, 0f, 1f).apply {
                    duration = 500
                }.start()
                currentView?.findViewById<ImageView>(R.id.cards_icon)?.isGone = false

                ObjectAnimator.ofFloat(currentView?.findViewById<TextView>(R.id.count_cards), View.ALPHA, 0f, 1f).apply {
                    duration = 500
                }.start()
                currentView?.findViewById<TextView>(R.id.count_cards)?.isGone = false

                ObjectAnimator.ofFloat(currentView?.findViewById<ImageView>(R.id.settings_icon), View.ALPHA, 1f, 0f).apply {
                    duration = 500
                }.start()
                currentView?.findViewById<ImageView>(R.id.settings_icon)?.isGone = true

                ObjectAnimator.ofFloat(currentView?.findViewById<ImageView>(R.id.play_icon), View.ALPHA, 1f, 0f).apply {
                    duration = 500
                }.start()
                currentView?.findViewById<ImageView>(R.id.play_icon)?.isGone = true

                itemView.background = itemView.resources.getDrawable(entry_list_click_background)

                ObjectAnimator.ofFloat(itemView.findViewById<ImageView>(R.id.settings_icon), View.ALPHA, 0f, 1f).apply {
                    duration = 500
                }.start()
                itemView.findViewById<ImageView>(R.id.settings_icon).isGone = false

                ObjectAnimator.ofFloat(itemView.findViewById<ImageView>(R.id.play_icon), View.ALPHA, 0f, 1f).apply {
                    duration = 500
                }.start()
                itemView.findViewById<ImageView>(R.id.play_icon).isGone = false

                ObjectAnimator.ofFloat(itemView.findViewById<ImageView>(R.id.cards_icon), View.ALPHA, 0f, 1f).apply {
                    duration = 500
                }.start()
                itemView.findViewById<ImageView>(R.id.cards_icon).isGone = true

                ObjectAnimator.ofFloat(itemView.findViewById<ImageView>(R.id.count_cards), View.ALPHA, 0f, 1f).apply {
                    duration = 500
                }.start()
                itemView.findViewById<TextView>(R.id.count_cards).isGone = true
                currentView = itemView
            }
        }
    }

    fun changeFocusFlashcard(itemView: View){
        if(firstUse) {
            itemView.background = itemView.resources.getDrawable(entry_list_click_background)

            ObjectAnimator.ofFloat(itemView.findViewById<ImageView>(R.id.entry_flashcard_delete), View.ALPHA, 0f, 1f).apply {
                duration = 500
            }.start()
            itemView.findViewById<ImageView>(R.id.entry_flashcard_delete).isGone = false
            currentView = itemView
            firstUse = false
        } else {
            if(itemView == currentView){
                currentView?.background = currentView?.resources?.getDrawable(entry_list_background)

                ObjectAnimator.ofFloat(itemView.findViewById<ImageView>(R.id.entry_flashcard_delete), View.ALPHA, 1f, 0f).apply {
                    duration = 500
                }.start()
                currentView?.findViewById<ImageView>(R.id.entry_flashcard_delete)?.isGone = true

                currentView = null
            } else {
                currentView?.background = currentView?.resources?.getDrawable(entry_list_background)

                ObjectAnimator.ofFloat(itemView.findViewById<ImageView>(R.id.entry_flashcard_delete), View.ALPHA, 1f, 0f).apply {
                    duration = 500
                }.start()
                currentView?.findViewById<ImageView>(R.id.entry_flashcard_delete)?.isGone = true
                itemView.background = itemView.resources.getDrawable(entry_list_click_background)

                ObjectAnimator.ofFloat(itemView.findViewById<ImageView>(R.id.entry_flashcard_delete), View.ALPHA, 0f, 1f).apply {
                    duration = 500
                }.start()
                itemView.findViewById<ImageView>(R.id.entry_flashcard_delete).isGone = false

                currentView = itemView
            }
        }
    }
}