package com.francislainy.buffl.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.firebase.ui.auth.AuthUI
import com.francislainy.buffl.R
import com.francislainy.buffl.activities.LearnExploreActivity
import com.francislainy.buffl.activities.LoginActivity
import com.francislainy.buffl.activities.MainActivity
import com.francislainy.buffl.model.Course
import com.francislainy.buffl.utils.DATA_COURSES
import com.francislainy.buffl.utils.objectToStringJson
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.gson.Gson
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.fragment_courses_list.*
import kotlinx.android.synthetic.main.row_courses_item.view.*
import timber.log.Timber

class CoursesListFragment : Fragment() {

    private lateinit var adapter: GroupAdapter<ViewHolder>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_courses_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = GroupAdapter()
        rvCourseCards.adapter = adapter

        /** https://github.com/lisawray/groupie/issues/183 */
        adapter.setOnItemClickListener { item, view ->

            val cItem = item as CourseItem

            val intent = Intent(context, LearnExploreActivity::class.java)
            intent.putExtra("objectString", objectToStringJson(cItem.c))
            startActivity(intent)
        }

        fetchCourses(adapter)

        tvLogout.setOnClickListener {

            logout()
        }
    }

    private fun fetchCourses(adapter: GroupAdapter<ViewHolder>) {
        val user = FirebaseAuth.getInstance().currentUser!!
        val userId = user.uid

        val database = FirebaseDatabase.getInstance()
        val myRef = database.reference.child(userId).child(DATA_COURSES)
        myRef.orderByKey()

        // Read from the database
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                adapter.clear()

                dataSnapshot.children.forEach {

                    val cardMap = it.value
                    val json = Gson().toJson(cardMap)
                    val course = Gson().fromJson<Course>(json, Course::class.java)

                    adapter.add(CourseItem(course))
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
            }

        }

        override fun getLayout() = R.layout.row_courses_item

    }

}
