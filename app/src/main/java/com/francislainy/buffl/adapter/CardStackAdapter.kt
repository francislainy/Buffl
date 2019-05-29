package com.francislainy.buffl.adapter

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.app.Activity
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.francislainy.buffl.R
import com.francislainy.buffl.model.Card
import com.francislainy.buffl.utils.DARK_THEME
import com.francislainy.buffl.utils.PRIVATE_MODE
import com.francislainy.buffl.utils.setBackgroundColorExt
import com.francislainy.buffl.utils.setTvTextColor
import kotlinx.android.synthetic.main.row_swipe_card_item.view.*
import java.util.ArrayList

class CardStackAdapter(private val activity: Activity) :
    RecyclerView.Adapter<CardStackAdapter.ViewHolder>() {

    private val cardList = ArrayList<Card>()
    private var callback: AdapterCallback? = null

    fun addItem(item: Card, adapterCallback: AdapterCallback) {

        cardList.add(item)
        notifyDataSetChanged()
        callback = adapterCallback
    }

    fun deleteItem(item: Card) {
        cardList.remove(item)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(inflater.inflate(R.layout.row_swipe_card_item, parent, false))
    }

    override fun getItemCount(): Int {
        return cardList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bind(position)
    }

    fun flipAnimation(v: View, card: Card) {

        /** https://stackoverflow.com/a/46111810/6654475 */
        val oa1 = ObjectAnimator.ofFloat(v.cvParent, "scaleX", 1f, 0f)
        val oa2 = ObjectAnimator.ofFloat(v.cvParent, "scaleX", 0f, 1f)
        oa1.interpolator = DecelerateInterpolator()
        oa2.interpolator = AccelerateDecelerateInterpolator()
        oa1.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)

                oa2.start()

                with(card) {
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

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(position: Int) {

            val size = cardList.size
            var randomNum = (0 until size).random()

            /**-----*/
            // todo: keep track of which numbers have already been shown - 15/05/19
            val randomNumberList = ArrayList<Int>()
            var stillLooking = true

            while (stillLooking) {
                if (randomNumberList.contains(randomNum)) { // If random number already taken try to find a new one
                    randomNum = (0 until size).random()
                } else {
                    randomNumberList.add(randomNum)
                    stillLooking = false
                }
            }
            /**-----*/

            val card = cardList[position] // todo: have random number here and on callback and get them

            itemView.tvCardTitle.text = card.cardQuestion

            val sharedPref = activity.getSharedPreferences(DARK_THEME, PRIVATE_MODE)

            if (sharedPref.getBoolean(DARK_THEME, true)) {
                itemView.tvCardTitle.setTvTextColor(R.color.white)
                itemView.cvParent.setBackgroundColorExt(R.color.blue_dark_theme_card)
            } else {
                itemView.tvCardTitle.setTvTextColor(R.color.black)
                itemView.cvParent.setBackgroundColorExt(R.color.white)
            }

//            itemView.tvCardTitle.setOnClickListener {
//                callback?.onClickCallback(card, itemView, position)
//            }

            itemView.cvParent.setOnClickListener { v ->
                flipAnimation(v, card)

                callback?.onClickCallback(card, itemView, position)

            }

        }

    }

    interface AdapterCallback {
        fun onClickCallback(itemModel: Card, itemView: View, position: Int)
    }
}