package com.francislainy.buffl.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.francislainy.buffl.R

class CourseDetailFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_course_detail, container, false)
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
