package com.francislainy.buffl.fragments


import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.DecelerateInterpolator

import com.francislainy.buffl.R
import com.francislainy.buffl.utils.toast
import kotlinx.android.synthetic.main.fragment_card_detail.*

class CardDetailFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_card_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvCardTitle.text = "start"

        clCardParent.setOnClickListener {

            flipAnimation()
        }

    }


    private fun flipAnimation() {

        /** https://stackoverflow.com/a/46111810/6654475 */
        val oa1 = ObjectAnimator.ofFloat(clCardParent, "scaleX", 1f, 0f)
        val oa2 = ObjectAnimator.ofFloat(clCardParent, "scaleX", 0f, 1f)
        oa1.interpolator = DecelerateInterpolator()
        oa2.interpolator = AccelerateDecelerateInterpolator()
        oa1.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                activity?.toast()

                oa2.start()

                tvCardTitle.text = when (tvCardTitle.text) {
                    "end" -> "start"
                    "start" -> "end"
                    else -> ""
                }
            }
        })

        oa1.start()
    }

}
