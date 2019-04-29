package com.francislainy.buffl.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.francislainy.buffl.R
import com.francislainy.buffl.model.Course
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.fragment_course_detail.*
import kotlinx.android.synthetic.main.row_cards_item.view.*

class CourseDetailFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_course_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        donutProgress.progress = "58".toFloat() // todo: remove hardcode
        donutProgress.text = "${donutProgress.progress.toInt()}%"
        // todo: have arc start from top

        val adapter = GroupAdapter<ViewHolder>()
        rvCards.adapter = adapter
        adapter.add(CardItem(Course("15")))
        adapter.add(CardItem(Course("5")))
        adapter.add(CardItem(Course("34")))
        adapter.add(CardItem(Course("23")))
        adapter.add(CardItem(Course("18")))
        adapter.add(CardItem(Course("2")))
    }

    class CardItem(val c: Course) : Item<ViewHolder>() {

        override fun bind(viewHolder: ViewHolder, position: Int) {

            with(viewHolder.itemView) {

                val cardsText = "${c.courseTitle} ${resources.getString(R.string.row_cards_tv_cards_text)}"
                tvCardsText.text = cardsText

                
            }

        }

        override fun getLayout(): Int {
            return R.layout.row_cards_item
        }
    }


    companion object {

        fun newInstance(param1: String, param2: String) =
            CourseDetailFragment().apply {
                arguments = Bundle().apply {
                    putString("oi", param1)
                    putString("oi2", param2)
                }
            }
    }

//    fun newInstance(objectString: String): CourseCollectionFragment {
//
//        val fragment = CourseCollectionFragment()
//        val args = Bundle()
//        args.putString("objectString", objectString)
//        fragment.arguments = args
//
//        return fragment
//    }
}
