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
import kotlinx.android.synthetic.main.fragment_learn.*
import kotlinx.android.synthetic.main.row_box_item.view.*

class LearnFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_learn, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        donutProgress.progress = "58".toFloat() // todo: remove hardcode
        donutProgress.text = "${donutProgress.progress.toInt()}%"
        // todo: have arc start from top

        val adapter = GroupAdapter<ViewHolder>()
        rvBox.adapter = adapter
        adapter.add(BoxItem(Course("15")))
        adapter.add(BoxItem(Course("5")))
        adapter.add(BoxItem(Course("34")))
        adapter.add(BoxItem(Course("23")))
        adapter.add(BoxItem(Course("18")))
        adapter.add(BoxItem(Course("2")))
    }

    class BoxItem(private val c: Course) : Item<ViewHolder>() {

        override fun bind(viewHolder: ViewHolder, position: Int) {

            with(viewHolder.itemView) {

                val boxText = "${c.courseTitle} ${resources.getString(R.string.row_box_tv_box_text)}"
                tvBoxText.text = boxText
            }

        }

        override fun getLayout(): Int {
            return R.layout.row_box_item
        }
    }

    companion object {

        fun newInstance(param1: String) =
            LearnFragment().apply {
                arguments = Bundle().apply {
                    putString("objectString", param1)
//                    putString("oi2", param2)
                }
            }
    }

}
