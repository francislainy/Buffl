package com.francislainy.buffl.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

import com.francislainy.buffl.R
import com.francislainy.buffl.activities.MainActivity
import com.francislainy.buffl.fragments.helper.BaseFragmentNonRootView
import com.francislainy.buffl.model.Course
import com.francislainy.buffl.utils.objectFromJsonString
import kotlinx.android.synthetic.main.fragment_course_pager_controller.*


/**https://www.raywenderlich.com/324-viewpager-tutorial-getting-started-in-kotlin*/

private const val MAX_VALUE = 2

class CoursePagerControllerFragment : BaseFragmentNonRootView() {

    private lateinit var pagerAdapter: ViewPagerAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_course_pager_controller, container, false)
    }

    private var jsonString: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        jsonString = arguments?.getString("objectString")
        val course = objectFromJsonString(jsonString, Course::class.java)

        pagerAdapter = ViewPagerAdapter(activity?.supportFragmentManager!!)
        viewPager.adapter = pagerAdapter
        tabs.setupWithViewPager(viewPager)
    }

    inner class ViewPagerAdapter internal constructor(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
            var fragment: Fragment? = null

            when (position) {
                0 -> fragment = LearnFragment.newInstance(jsonString!!)
                1 -> fragment = ExploreFragment()
            }

            return fragment!!
        }

        override fun getCount(): Int {
            return MAX_VALUE
        }
    }

    companion object {

        fun newInstance(objectString: String): CoursePagerControllerFragment {

            val fragment = CoursePagerControllerFragment()
            val args = Bundle()
            args.putString("objectString", objectString)
            fragment.arguments = args

            return fragment
        }
    }

}