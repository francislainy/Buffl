package com.francislainy.buffl.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.francislainy.buffl.R
import com.francislainy.buffl.activities.MainActivity
import com.francislainy.buffl.fragments.helper.BaseFragmentNonRootView
import kotlinx.android.synthetic.main.fragment_course_collection.*

class CourseCollectionFragment : BaseFragmentNonRootView() {

    override fun onResume() {
        super.onResume()

        (activity as MainActivity).displayToolbar(2) //todo: have dynamic position -21/04/19
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_course_collection, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        val jsonString = arguments?.getString("objectString")
//        val course = objectFromJsonString(jsonString, Course::class.java)

        tvTest.text = "oi"
    }

    companion object {

        fun newInstance(objectString: String): CourseCollectionFragment {

            val fragment = CourseCollectionFragment()
            val args = Bundle()
            args.putString("objectString", objectString)
            fragment.arguments = args

            return fragment
        }
    }

}