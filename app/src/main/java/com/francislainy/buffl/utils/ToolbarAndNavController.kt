package com.francislainy.buffl.utils

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.francislainy.buffl.R
import com.francislainy.buffl.activities.MainActivity
import com.francislainy.buffl.fragments.CourseDetailFragment
import com.francislainy.buffl.fragments.CoursesListFragment
import kotlinx.android.synthetic.main.toolbar_widget.*

const val FRAG_HOME = 10

class ToolbarAndNavController(private val mainActivity: MainActivity) {

    fun toolbarSetUP(pos: Int) {

        with(mainActivity) {

            supportActionBar!!.setDisplayHomeAsUpEnabled(true)

            tvToolBarTitle.text = "Library"
            toolbarWithHamburger()
        }
    }

    private fun toolbarWithHamburger() {
        mainActivity.supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_action_menu)
    }

    private fun toolbarWithBackArrow() {
        mainActivity.supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_action_back)
    }

    fun replaceFragment(pos: Int) {
        val fragmentManager: FragmentManager = mainActivity.supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        val f: Fragment = when (pos) {
            FRAG_HOME -> CoursesListFragment()
            else -> CourseDetailFragment()
        }

        if (shouldBeAddedToStackNavigation(pos.toString())) {

            // for History back Navigation ;)
            fragmentTransaction.addToBackStack(pos.toString())

            // animation :)
            fragmentTransaction.setCustomAnimations(
                R.anim.enter_from_right,
                R.anim.exit_to_left,
                R.anim.enter_from_left,
                R.anim.exit_to_right
            )

        } else {

            // if is one of the 5 Main Tabs (Home, Fitness, Goals, Rewards, Nutrition)

            // don't do the animation then ;)
        }

        fragmentTransaction.replace(R.id.container_body_main, f, pos.toString())

        fragmentTransaction.commit()
    }

    // The 5 main Tabs (Home, Fitness, Goals, Rewards, Nutrition)
    private fun shouldBeAddedToStackNavigation(TAG: String): Boolean = when (TAG) {

        "FRAG_HOME", "FRAG_LOCATION", "FRAG_TIC_TAC", "HOME_REWARDS", "HOME_NUTRITION" -> false

        else -> true
    }

}