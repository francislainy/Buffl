package com.francislainy.buffl.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.francislainy.buffl.R
import com.francislainy.buffl.fragments.helper.BaseFragmentNonRootView
import com.francislainy.buffl.model.Card
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.fragment_explore.*
import kotlinx.android.synthetic.main.row_card_item.view.*
import timber.log.Timber
import com.google.gson.Gson


class ExploreFragment : BaseFragmentNonRootView() {

    override fun onResume() {
        super.onResume()

//        (activity as MainActivity).displayToolbar(2) //todo: have dynamic position -21/04/19
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_explore, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        val jsonString = arguments?.getString("objectString")
//        val course = objectFromJsonString(jsonString, Course::class.java)

        val adapter = GroupAdapter<ViewHolder>()
        rvCards.adapter = adapter
        fetchCards(adapter)


//        adapter.add(CardItem(Card("God is not unrighteous to forget the love you've shown")))
//        adapter.add(CardItem(Course("5")))
//        adapter.add(CardItem(Course("34")))
//        adapter.add(CardItem(Course("23")))
//        adapter.add(CardItem(Course("18")))
//        adapter.add(CardItem(Course("77")))
    }

    private fun fetchCards(adapter: GroupAdapter<ViewHolder>) {
        val user = FirebaseAuth.getInstance().currentUser!!
        val userId = user.uid

        val database = FirebaseDatabase.getInstance()
        val myRef = database.reference.child(userId).child("cards")
        myRef.orderByKey()

        // Read from the database
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                adapter.clear()

                dataSnapshot.children.forEach {

                    val answer = it.child("cardAnswer").value as String
                    val question = it.child("cardQuestion").value as String
                    val courseId = it.child("courseId").value as String

//                    val cardDs = dataSnapshot.children as Card
                    val cardDs = dataSnapshot.getValue(Card::class.java)
                    val json = Gson().toJson(cardDs)
                    val card: Card = Gson().fromJson<Card>(json, Card::class.java)
                    card.cardAnswer = answer
                    card.cardQuestion = question
                    card.courseId = courseId

                    adapter.add(CardItem(card))
                }

            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Timber.w("Failed to read value. + ${error.toException()}")
            }
        })
    }

    class CardItem(private val c: Card) : Item<ViewHolder>() {

        override fun bind(viewHolder: ViewHolder, position: Int) {

            with(viewHolder.itemView) {

                val cardsText = c.cardQuestion
                tvCardTitle.text = cardsText
            }

        }

        override fun getLayout(): Int {
            return R.layout.row_card_item
        }
    }


    companion object {

        fun newInstance(objectString: String): ExploreFragment {

            val fragment = ExploreFragment()
            val args = Bundle()
            args.putString("objectString", objectString)
            fragment.arguments = args

            return fragment
        }
    }

}