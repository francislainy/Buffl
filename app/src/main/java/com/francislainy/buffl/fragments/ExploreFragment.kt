package com.francislainy.buffl.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.francislainy.buffl.R
import com.francislainy.buffl.activities.MainActivity
import com.francislainy.buffl.fragments.helper.BaseFragmentNonRootView
import com.francislainy.buffl.model.Course
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.fragment_explore.*
import kotlinx.android.synthetic.main.row_card_item.view.*

class ExploreFragment : BaseFragmentNonRootView() {

    override fun onResume() {
        super.onResume()

//        (activity as MainActivity).displayToolbar(2) //todo: have dynamic position -21/04/19
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_explore, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        val jsonString = arguments?.getString("objectString")
//        val course = objectFromJsonString(jsonString, Course::class.java)

//        tvTest.text = "oi"

        val adapter = GroupAdapter<ViewHolder>()
        rvCards.adapter = adapter
        adapter.add(CardItem(Course("God is not unrighteous to forget the love you've shown")))
        adapter.add(CardItem(Course("5")))
        adapter.add(CardItem(Course("34")))
        adapter.add(CardItem(Course("23")))
        adapter.add(CardItem(Course("18")))
        adapter.add(CardItem(Course("2")))
    }

    class CardItem(private val c: Course) : Item<ViewHolder>() {

        override fun bind(viewHolder: ViewHolder, position: Int) {

            with(viewHolder.itemView) {

                val cardsText = c.courseTitle
                tvCardTitle.text = cardsText
            }

        }

        override fun getLayout(): Int {
            return R.layout.row_card_item
        }
    }


    companion object {

        fun newInstance(objectString: String): ExploreFragment {

            val fragment = ExploreFragment()
            val args = Bundle()
            args.putString("objectString", objectString)
            fragment.arguments = args

            return fragment
        }
    }

}