package com.francislainy.buffl.fragments


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.firebase.ui.auth.AuthUI
import com.francislainy.buffl.R
import com.francislainy.buffl.activities.LoginActivity
import com.francislainy.buffl.activities.MainActivity
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.fragment_courses_list.*

class CoursesListFragment : Fragment() {

    override fun onResume() {
        super.onResume()

        (activity as MainActivity).displayToolbar(1) //todo: dynamic pos -21/04/19
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_courses_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = GroupAdapter<ViewHolder>()
        adapter.add(UserItem(""))
        adapter.add(UserItem(""))
        adapter.add(UserItem(""))
        adapter.add(UserItem(""))
        adapter.add(UserItem(""))
        rvCourseCards.adapter = adapter

        tvLogout.setOnClickListener { logout() }
    }

    private fun logout() {
        AuthUI.getInstance().signOut(activity as MainActivity)
            .addOnCompleteListener {
                // user is now signed out
                activity?.startActivity(Intent(activity as MainActivity, LoginActivity::class.java))
                activity?.finish()
            }
    }

    class UserItem(val text: String) : Item<ViewHolder>() {

        override fun bind(viewHolder: ViewHolder, position: Int) {

            with(viewHolder.itemView) {

//                tvCollectionTitle.text = "Test"
//                tvUsername.text = user?.username
            }
        }

        override fun getLayout(): Int {
            return R.layout.row_courses_item
        }
    }
}
