package com.francislainy.buffl.adapter

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.francislainy.buffl.R
import com.francislainy.buffl.model.Card
import kotlinx.android.synthetic.main.row_swipe_card_item.view.*

class CardStackAdapter(private var cardList: List<Card> = emptyList()) :
    RecyclerView.Adapter<CardStackAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(inflater.inflate(R.layout.row_swipe_card_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val size = cardList.size
        val randomNum = (0 until size).random()

        holder.itemView.tvCardTitle.text = cardList[randomNum].cardQuestion
        holder.itemView.cvParent.setOnClickListener { v ->
            flipAnimation(v, randomNum)
        }
    }

    private fun flipAnimation(v: View, randomNum: Int) {

        /** https://stackoverflow.com/a/46111810/6654475 */
        val oa1 = ObjectAnimator.ofFloat(v.cvParent, "scaleX", 1f, 0f)
        val oa2 = ObjectAnimator.ofFloat(v.cvParent, "scaleX", 0f, 1f)
        oa1.interpolator = DecelerateInterpolator()
        oa2.interpolator = AccelerateDecelerateInterpolator()
        oa1.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)

                oa2.start()

                with(cardList[randomNum]) {
                    v.tvCardTitle.text = when (v.tvCardTitle.text) {
                        cardQuestion -> cardAnswer
                        cardAnswer -> cardQuestion
                        else -> ""
                    }
                }
            }
        })

        oa1.start()
    }

    override fun getItemCount(): Int {
        return cardList.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvCardTitle: TextView = view.findViewById(R.id.tvCardTitle)
    }


}