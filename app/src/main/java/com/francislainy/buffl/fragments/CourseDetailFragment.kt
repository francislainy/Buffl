package com.francislainy.buffl.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.francislainy.buffl.R
import kotlinx.android.synthetic.main.fragment_course_detail.*

class CourseDetailFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_course_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        donutProgress.progress = "58".toFloat() // todo: remove hardcode
        donutProgress.text = "${donutProgress.progress.toInt()}%"
        // todo: have arc start from top
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
