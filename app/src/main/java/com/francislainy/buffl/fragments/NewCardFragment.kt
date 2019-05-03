package com.francislainy.buffl.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.francislainy.buffl.R
import com.francislainy.buffl.utils.toast
import kotlinx.android.synthetic.main.fragment_new_card.*
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.animation.ObjectAnimator


class NewCardFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_new_card, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnFront.setOnClickListener(onClickFrontBackButton)
        btnBack.setOnClickListener(onClickFrontBackButton)
    }

    private val onClickFrontBackButton = View.OnClickListener { view ->

        when (view?.id) {
            R.id.btnFront -> {

                etTypeHere.hint = "Type in Question..."

                btnFront.setTextColor(resources.getColor(R.color.black))
                btnBack.setTextColor(resources.getColor(R.color.dark_grey_aaa))
                btnFront.setBackgroundResource(R.drawable.shape_rectangle_bottom_border_green)
                btnBack.setBackgroundResource(R.drawable.shape_rectangle_bottom_border_same_grey)
            }
            R.id.btnBack -> {

                etTypeHere.hint = "Type in Answer..."

                btnBack.setTextColor(resources.getColor(R.color.black))
                btnFront.setTextColor(resources.getColor(R.color.dark_grey_aaa))
                btnBack.setBackgroundResource(R.drawable.shape_rectangle_bottom_border_green)
                btnFront.setBackgroundResource(R.drawable.shape_rectangle_bottom_border_same_grey)
            }
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
            }
        })
        oa1.start()
    }

}
