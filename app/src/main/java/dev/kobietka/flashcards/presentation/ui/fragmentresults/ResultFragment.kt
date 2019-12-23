package dev.kobietka.flashcards.presentation.ui.fragmentresults

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import dev.kobietka.flashcards.R
import dev.kobietka.flashcards.presentation.ui.common.BaseFragment
import dev.kobietka.flashcards.presentation.ui.fragmentmain.MainFragment

class ResultFragment : BaseFragment() {

    private lateinit var starsImage: ImageView
    private lateinit var scoreText: TextView
    private lateinit var backButton: RelativeLayout
    private lateinit var staticText: TextView
    var score = 0
    var maxScore = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        starsImage = view.findViewById(R.id.results_stars)
        scoreText = view.findViewById(R.id.results_score)
        backButton = view.findViewById(R.id.results_back_button)
        staticText = view.findViewById(R.id.results_text)

        val division = (score.toDouble()/maxScore.toDouble())

        scoreText.text = " $score/$maxScore"

        when {
            division < 0.5 -> {
                staticText.text = "You will do better next time!"
            }
            division < 0.75 -> {
                staticText.text = "Nice!"
                starsImage.setImageDrawable(resources.getDrawable(R.drawable.ic_1stars))
            }
            division < 0.9 -> {
                staticText.text = "Very good!"
                starsImage.setImageDrawable(resources.getDrawable(R.drawable.ic_2stars))
            }
            division < 1.0 -> {
                staticText.text = "Almost Perfect!"
                starsImage.setImageDrawable(resources.getDrawable(R.drawable.ic_3stars))
            }
            division == 1.0 -> {
                staticText.text = "Perfect!"
                starsImage.setImageDrawable(resources.getDrawable(R.drawable.ic_3stars))
            }
        }

        backButton.setOnClickListener {
            Log.e("SCORE", score.toString())
            Log.e("MAXSCORE", maxScore.toString())
            Log.e("SCORE?MAX",(score.toDouble()/maxScore).toString() )
            activity?.supportFragmentManager?.beginTransaction()
                ?.setCustomAnimations(R.anim.enter_left_to_right, R.anim.exit_left_to_right)
                ?.replace(R.id.main_container, MainFragment())
                ?.commit()
        }
    }

    override fun getLayout(): Int {
        return R.layout.fragment_results
    }
}