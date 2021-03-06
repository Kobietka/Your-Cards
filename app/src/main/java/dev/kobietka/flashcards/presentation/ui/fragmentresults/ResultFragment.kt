package dev.kobietka.flashcards.presentation.ui.fragmentresults

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.view.isGone
import dev.kobietka.flashcards.R
import dev.kobietka.flashcards.presentation.ui.common.BaseFragment

class ResultFragment : BaseFragment() {

    private lateinit var starsImage: ImageView
    private lateinit var scoreText: TextView
    private lateinit var backButton: RelativeLayout
    private lateinit var staticText: TextView
    var ratio = 0.0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        starsImage = view.findViewById(R.id.results_stars)
        scoreText = view.findViewById(R.id.results_score)
        backButton = view.findViewById(R.id.results_back_button)
        staticText = view.findViewById(R.id.results_text)

        val percentage = (ratio * 100).toInt()

        scoreText.text = "$percentage%"

        ObjectAnimator.ofFloat(scoreText, View.ALPHA, 0f, 1f).apply {
            duration = 500
        }.start()
        scoreText.isGone = false

        when {
            ratio < 0.5 -> {
                staticText.text = "You will do better next time!"
            }
            ratio < 0.75 -> {
                staticText.text = "Nice!"
                starsImage.setImageDrawable(resources.getDrawable(R.drawable.ic_1stars))
            }
            ratio < 0.9 -> {
                staticText.text = "Very good!"
                starsImage.setImageDrawable(resources.getDrawable(R.drawable.ic_2stars))
            }
            ratio < 1.0 -> {
                staticText.text = "Almost Perfect!"
                starsImage.setImageDrawable(resources.getDrawable(R.drawable.ic_3stars))
            }
            ratio == 1.0 -> {
                staticText.text = "Perfect!"
                starsImage.setImageDrawable(resources.getDrawable(R.drawable.ic_3stars))
            }
        }

        ObjectAnimator.ofFloat(backButton, View.ALPHA, 0f, 1f).apply {
            duration = 500
        }.start()
        backButton.isGone = false

        backButton.setOnClickListener {
            val fragment = activity!!.supportFragmentManager.findFragmentByTag("mainFragment")
            activity!!.supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.exit_right_to_left, R.anim.enter_right_to_left)
                .replace(R.id.main_container, fragment!!)
                .commit()
        }
    }

    override fun getLayout(): Int {
        return R.layout.fragment_results
    }
}