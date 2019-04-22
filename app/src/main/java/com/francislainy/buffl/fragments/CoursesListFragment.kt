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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.custom_add_dialog.*
import kotlinx.android.synthetic.main.fragment_courses_list.*
import kotlinx.android.synthetic.main.row_courses_item.view.*
import timber.log.Timber


class CoursesListFragment : Fragment() {

    private lateinit var myRef: DatabaseReference
    private lateinit var database: FirebaseDatabase
    private lateinit var user: FirebaseUser
    private lateinit var userId: String

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

        tvLogout.setOnClickListener { logout() }

        user = FirebaseAuth.getInstance().currentUser!!
        userId = user.uid

        database = FirebaseDatabase.getInstance()
        myRef = database.reference.child(userId).child("courses")
        myRef.orderByKey()

        // Read from the database
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                adapter.clear()

                for (ds in dataSnapshot.children) {

//                        val map = ds.value as Map<String, String> //todo: keep order for items as they are added

//                        for ((key, value) in map) {
                    adapter.add(UserItem(ds.value.toString())) //todo: have object instead of string to allow for more data
//                        }

                    Timber.d("value is: $ds.value")
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

    class UserItem(val text: String) : Item<ViewHolder>() {

        override fun bind(viewHolder: ViewHolder, position: Int) {

            with(viewHolder.itemView) {

                tvCollectionTitle.text = text
            }
        }

        override fun getLayout(): Int {
            return R.layout.row_courses_item
        }
    }
}
