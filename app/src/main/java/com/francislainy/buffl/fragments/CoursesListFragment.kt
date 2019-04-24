package com.francislainy.buffl.fragments


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.firebase.ui.auth.AuthUI
import com.francislainy.buffl.R
import com.francislainy.buffl.activities.LoginActivity
import com.francislainy.buffl.activities.MainActivity
import com.francislainy.buffl.model.Course
import com.francislainy.buffl.utils.Utils
import com.francislainy.buffl.utils.objectToStringJson
import com.francislainy.buffl.utils.toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.fragment_courses_list.*
import kotlinx.android.synthetic.main.row_courses_item.view.*
import timber.log.Timber


class CoursesListFragment : Fragment() {

    override fun onResume() {
        super.onResume()

        (activity as MainActivity).displayToolbar(1) //todo: have dynamic position -21/04/19
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_courses_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = GroupAdapter<ViewHolder>()
        rvCourseCards.adapter = adapter

        // https://github.com/lisawray/groupie/issues/183
        adapter.setOnItemClickListener { item, view ->

            val cItem = item as CourseItem

            (activity as MainActivity).displayView(1, objectToStringJson(cItem.c)) //todo: have dynamic position -21/04/19
        }

        fetchCourses(adapter)

        tvLogout.setOnClickListener { logout() }
    }

    private fun fetchCourses(adapter: GroupAdapter<ViewHolder>) {
        val user = FirebaseAuth.getInstance().currentUser!!
        val userId = user.uid

        val database = FirebaseDatabase.getInstance()
        val myRef = database.reference.child(userId).child("courses")
        myRef.orderByKey()

        // Read from the database
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                adapter.clear()

                dataSnapshot.children.forEach {

                    @Suppress("UNCHECKED_CAST")
                    val map = it.value as Map<String, String>

                    @Suppress("UNUSED_VARIABLE")
                    for ((key, value) in map) {

                        adapter.add(CourseItem(Course(value)))
                    }
                }

            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Timber.w("Failed to read value. + ${error.toException()}")
            }
        })
    }

    private fun logout() {
        AuthUI.getInstance().signOut(activity as MainActivity)
            .addOnCompleteListener {
                // user is now signed out
                activity?.startActivity(Intent(activity as MainActivity, LoginActivity::class.java))
                activity?.finish()
            }
    }

    class CourseItem(val c: Course) : Item<ViewHolder>() {

        override fun bind(viewHolder: ViewHolder, position: Int) {

            with(viewHolder.itemView) {

                tvCollectionTitle.text = c.courseTitle

//                tvCollectionTitle.setOnClickListener {
//                    context.toast("clicked")
//                }
            }

        }

        override fun getLayout(): Int {
            return R.layout.row_courses_item
        }
    }
}
