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
import com.francislainy.buffl.model.Card
import com.francislainy.buffl.model.Course
import com.francislainy.buffl.model.MySet
import com.francislainy.buffl.utils.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.gson.Gson
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.fragment_set_list.*
import kotlinx.android.synthetic.main.row_set_item.view.*
import timber.log.Timber

class SetListFragment : Fragment() {

    private lateinit var adapter: GroupAdapter<ViewHolder>
    private var course: Course? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_set_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val courseString = arguments?.getString("courseString")
        course = objectFromJsonString(courseString, Course::class.java)

        adapter = GroupAdapter()
        rvSetCards.adapter = adapter

        /** https://github.com/lisawray/groupie/issues/183 */
        adapter.setOnItemClickListener { item, view ->

            val cItem = item as SetItem

            val intent = Intent(context, LearnExploreActivity::class.java)
            intent.putExtra("setString", objectToStringJson(cItem.set))
            startActivity(intent)
        }

        fetchSets(adapter)
    }

    private fun fetchSets(adapter: GroupAdapter<ViewHolder>) {
        val user = FirebaseAuth.getInstance().currentUser!!
        val userId = user.uid

        val database = FirebaseDatabase.getInstance()
        val myRef = database.reference.child(userId).child(DATA_SETS)
        myRef.orderByKey()

        // Read from the database
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                adapter.clear()

                dataSnapshot.children.forEach {

                    val map = it.value
                    val json = Gson().toJson(map)
                    val set = Gson().fromJson<MySet>(json, MySet::class.java)

                    if (set.courseId == course?.courseId) {

                        fetchCards(set, set.setId)

                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Timber.w("Failed to read value. + ${error.toException()}")
            }
        })
    }


    private fun fetchCards(set: MySet, setId: String) {
        val user = FirebaseAuth.getInstance().currentUser!!
        val userId = user.uid

        val database = FirebaseDatabase.getInstance()
        val myRef = database.reference.child(userId).child(DATA_CARDS)
        myRef.orderByKey()

        var cardsAmount = 0

        // Read from the database
        myRef.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {

                dataSnapshot.children.forEach {

                    val map = it.value
                    val json = Gson().toJson(map)
                    val card = Gson().fromJson<Card>(json, Card::class.java)

                    if (card.setId == setId) {
                        cardsAmount++
                    }

                }

                adapter.add(SetItem(set, cardsAmount))

            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Timber.w("Failed to read value. + ${error.toException()}")
            }
        })
    }

    inner class SetItem(val set: MySet, private val cardsAmount: Int) : Item<ViewHolder>() {

        override fun bind(viewHolder: ViewHolder, position: Int) {

            with(viewHolder.itemView) {

                tvSetTitle.text = set.setTitle
                tvCourseTitle.text = course?.courseTitle
                tvCardsAmount.text = cardsAmount.toString()
            }
        }

        override fun getLayout() = R.layout.row_set_item

    }

    companion object {

        fun newInstance(param1: String) =
            SetListFragment().apply {
                arguments = Bundle().apply {
                    putString("courseString", param1)
                }
            }
    }
}
