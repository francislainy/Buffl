package com.francislainy.buffl.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

import com.francislainy.buffl.R
import com.francislainy.buffl.fragments.helper.BaseFragmentNonRootView
import kotlinx.android.synthetic.main.fragment_course_pager_controller.*


/**https://www.raywenderlich.com/324-viewpager-tutorial-getting-started-in-kotlin*/

private const val MAX_VALUE = 2

class SetPagerControllerFragment : BaseFragmentNonRootView() {

    private lateinit var pagerAdapter: ViewPagerAdapter
    private var setString: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_course_pager_controller, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setString = arguments?.getString("setString")

        pagerAdapter = ViewPagerAdapter(activity?.supportFragmentManager!!)
        viewPager.adapter = pagerAdapter
        tabs.setupWithViewPager(viewPager)
    }

    inner class ViewPagerAdapter internal constructor(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
            var fragment: Fragment? = null

            when (position) {
                0 -> fragment = LearnFragment.newInstance(setString!!)
                1 -> fragment = ExploreFragment.newInstance(setString!!)
            }

            return fragment!!
        }

        override fun getCount(): Int {
            return MAX_VALUE
        }
    }

    companion object {

        fun newInstance(setString: String): SetPagerControllerFragment {

            val fragment = SetPagerControllerFragment()
            val args = Bundle()
            args.putString("setString", setString)
            fragment.arguments = args

            return fragment
        }
    }

}