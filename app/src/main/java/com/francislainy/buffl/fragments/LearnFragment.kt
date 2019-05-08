package com.francislainy.buffl.fragments

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import androidx.core.content.ContextCompat.startActivity

import com.francislainy.buffl.R
import com.francislainy.buffl.activities.CardDetailActivity
import com.francislainy.buffl.model.Card
import com.francislainy.buffl.model.Course
import com.francislainy.buffl.utils.objectToStringJson
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.fragment_learn.*
import kotlinx.android.synthetic.main.row_box_item.view.*
import timber.log.Timber
import java.util.ArrayList

class LearnFragment : Fragment() {

    private val adapter = GroupAdapter<ViewHolder>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_learn, container, false)
    }

    override fun onResume() {
        super.onResume()

        fetchData(adapter)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        donutProgress.progress = "58".toFloat() // todo: remove hardcode
        donutProgress.text = "${donutProgress.progress.toInt()}%"
        donutProgressAnimation()

        rvBox.adapter = adapter
        fetchData(adapter)
    }

    private fun fetchData(adapter: GroupAdapter<ViewHolder>) {
        val user = FirebaseAuth.getInstance().currentUser!!
        val userId = user.uid

        val database = FirebaseDatabase.getInstance()
        val myRef = database.reference.child(userId).child("cards")
        myRef.orderByKey()

        adapter.clear()
        val map0 = HashMap<ArrayList<Card>, Int>()
        val map1 = HashMap<ArrayList<Card>, Int>()
        val map2 = HashMap<ArrayList<Card>, Int>()
        val map3 = HashMap<ArrayList<Card>, Int>()
        val map4 = HashMap<ArrayList<Card>, Int>()
        val map5 = HashMap<ArrayList<Card>, Int>()
        val lists0 = ArrayList<Card>()
        val lists1 = ArrayList<Card>()
        val lists2 = ArrayList<Card>()
        val lists3 = ArrayList<Card>()
        val lists4 = ArrayList<Card>()
        val lists5 = ArrayList<Card>()

        // Read from the database
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                dataSnapshot.children.forEach {

                    val cardMap = it.value
                    val json = Gson().toJson(cardMap)
                    val card = Gson().fromJson<Card>(json, Card::class.java)

                    when (card.cardPosition) {
                        0 -> lists0.add(card)
                        1 -> lists1.add(card)
                        2 -> lists2.add(card)
                        3 -> lists3.add(card)
                        4 -> lists4.add(card)
                        5 -> lists5.add(card)
                    }

                }

                map0.set(lists0, 0)
                map1.set(lists1, 1)
                map2.set(lists2, 2)
                map3.set(lists3, 3)
                map4.set(lists4, 4)
                map5.set(lists5, 5)

                adapter.add(BoxItem(map0, lists0))
                adapter.add(BoxItem(map1, lists1))
                adapter.add(BoxItem(map2, lists2))
                adapter.add(BoxItem(map3, lists3))
                adapter.add(BoxItem(map4, lists4))
                adapter.add(BoxItem(map5, lists5))
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Timber.w("Failed to read value. + ${error.toException()}")
            }
        })
    }

    class BoxItem(private val m: Map<ArrayList<Card>, Int>, private val list: ArrayList<Card>) : Item<ViewHolder>() {

        override fun bind(viewHolder: ViewHolder, position: Int) {

            with(viewHolder.itemView) {

                for (i in m) {

                    val boxText = when (i.key.size) {
                        1 -> "${i.key.size} ${resources.getString(R.string.row_box_tv_box_text_singular)}"
                        else -> {
                            "${i.key.size} ${resources.getString(R.string.row_box_tv_box_text)}"
                        }
                    }

                    when (i.key.size) {
                        0 -> tvBoxText.setTextColor(resources.getColor(R.color.dark_grey_aaa))
                        else -> tvBoxText.setTextColor(resources.getColor(R.color.colorPrimary))
                    }

                    when (i.value) {

                        5 -> { // Last box (completion)
                            ivTick.visibility = View.VISIBLE
                            tvBoxNumber.visibility = View.INVISIBLE
                        }
                        else -> {
                            ivTick.visibility = View.INVISIBLE
                            tvBoxNumber.visibility = View.VISIBLE
                        }
                    }

                    tvBoxText.text = boxText
                }

                clParent.setOnClickListener {
                    val json = objectToStringJson(list)
                    val intent = Intent(context, CardDetailActivity::class.java).apply {
                        putExtra("objectString", json)
                    }

                    if (list.size > 0) {
                        context.startActivity(intent)
                    }
                }
            }

        }

        override fun getLayout(): Int {
            return R.layout.row_box_item
        }
    }

    private fun donutProgressAnimation() {
        activity!!.runOnUiThread {
            val anim = ObjectAnimator.ofFloat(donutProgress, "progress", 0f, 58f)
            anim.interpolator = DecelerateInterpolator()
            anim.duration = 2000
            anim.start()

            val listObjectAnimator = ArrayList<ObjectAnimator>()
            listObjectAnimator.add(anim)
        }
    }

    companion object {

        fun newInstance(param1: String) =
            LearnFragment().apply {
                arguments = Bundle().apply {
                    putString("objectString", param1)
//                    putString("oi2", param2)
                }
            }
    }

}
