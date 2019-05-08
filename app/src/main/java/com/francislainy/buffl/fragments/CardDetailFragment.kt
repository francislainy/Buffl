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
import android.view.animation.LinearInterpolator
import androidx.recyclerview.widget.DefaultItemAnimator

import com.francislainy.buffl.R
import com.francislainy.buffl.activities.CardDetailActivity
import com.francislainy.buffl.adapter.CardStackAdapter
import com.francislainy.buffl.model.Card
import com.francislainy.buffl.utils.toast
import kotlinx.android.synthetic.main.fragment_card_detail.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.yuyakaido.android.cardstackview.*

class CardDetailFragment : Fragment(), CardStackListener {
    override fun onCardDisappeared(view: View?, position: Int) {
    }

    override fun onCardDragging(direction: Direction?, ratio: Float) {
    }

    override fun onCardSwiped(direction: Direction?) {
    }

    override fun onCardCanceled() {
    }

    override fun onCardAppeared(view: View?, position: Int) {
    }

    override fun onCardRewound() {
    }

    private var cardList: List<Card>? = null
    private var randomNum: Int = 0
    private val manager by lazy { CardStackLayoutManager(activity as CardDetailActivity, this) }
    private val adapter by lazy { CardStackAdapter(cardList!!) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_card_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val jsonString = arguments?.getString("objectString")

        /** https://stackoverflow.com/a/9599112/6654475 */
        val collectionType = object : TypeToken<List<Card>>() {}.type
        cardList = Gson().fromJson(jsonString, collectionType) as List<Card>

        val size = cardList!!.size
        randomNum = (0 until size).random()

        setupCardStackView()


//        tvCardTitle.text = cardList!![randomNum].cardQuestion

        cardStackView.setOnClickListener {

//            flipAnimation()
        }
    }

    private fun setupCardStackView() {
        initialize()
    }

    private fun initialize() {
        manager.apply {
            setStackFrom(StackFrom.None)
            setVisibleCount(3)
            setTranslationInterval(8.0f)
            setScaleInterval(0.95f)
            setSwipeThreshold(0.3f)
            setMaxDegree(20.0f)
            setDirections(Direction.HORIZONTAL)
            setCanScrollHorizontal(true)
            setCanScrollVertical(true)
            setSwipeableMethod(SwipeableMethod.AutomaticAndManual)
            setOverlayInterpolator(LinearInterpolator())
        }
        cardStackView.layoutManager = manager
        cardStackView.adapter = adapter
        cardStackView.itemAnimator.apply {
            if (this is DefaultItemAnimator) {
                supportsChangeAnimations = false
            }
        }
    }

    companion object {

        fun newInstance(objectString: String): CardDetailFragment {

            val fragment = CardDetailFragment()
            fragment.arguments = Bundle().apply {
                putString("objectString", objectString)
            }

            return fragment
        }
    }

//    companion object {
//
//        fun newInstance(param1: String) =
//            CardDetailFragment().apply {
//                arguments = Bundle().apply {
//                    putString("objectString", param1)
//                }
//            }
//    }
}
