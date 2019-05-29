package com.francislainy.buffl.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import androidx.recyclerview.widget.DefaultItemAnimator

import com.francislainy.buffl.R
import com.francislainy.buffl.activities.CardDetailActivity
import com.francislainy.buffl.adapter.CardStackAdapter
import com.francislainy.buffl.model.Card
import com.francislainy.buffl.model.Course
import com.francislainy.buffl.model.MySet
import com.francislainy.buffl.utils.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_card_detail.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.yuyakaido.android.cardstackview.*
import kotlinx.android.synthetic.main.row_swipe_card_item.view.*

@SuppressLint("CommitPrefEdits")
class CardDetailFragment : Fragment(), CardStackListener, CardStackAdapter.AdapterCallback {
    override fun onClickCallback(itemModel: Card, itemView: View, position: Int) {
        stringTest = itemModel.cardQuestion
        this.itemModel = itemModel
        this.itemView = itemView
        this.position = position
    }

    override fun onCardDisappeared(view: View?, position: Int) {
    }

    override fun onCardDragging(direction: Direction?, ratio: Float) {
    }

    override fun onCardSwiped(direction: Direction?) {
//        itemView?.tvCardTitle?.performClick()
//
//        adapter.flipAnimation(this.itemView!!, this.itemModel!!)
    }

    override fun onCardCanceled() {
    }

    override fun onCardAppeared(view: View?, position: Int) {
    }

    override fun onCardRewound() {
    }

    private val sharedPref by lazy { activity!!.getSharedPreferences(DARK_THEME, PRIVATE_MODE) }
    private val editor by lazy { sharedPref.edit() }

    private var itemModel: Card? = null
    private var itemView: View? = null
    private var position: Int? = 0

    private var stringTest: String? = null
    private var setString: String? = null
    private var cardList: List<Card>? = null
    private val manager by lazy { CardStackLayoutManager(activity as CardDetailActivity, this) }
    private val adapter by lazy { CardStackAdapter(activity as CardDetailActivity) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return if (sharedPref.getBoolean(DARK_THEME, true)) {
            inflater.inflate(R.layout.fragment_card_detail_dark, container, false)
        } else {
            inflater.inflate(R.layout.fragment_card_detail, container, false)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setString = arguments?.getString("setString")

        /** https://stackoverflow.com/a/9599112/6654475 */
        val collectionType = object : TypeToken<List<Card>>() {}.type
        cardList = Gson().fromJson(setString, collectionType) as List<Card>

        for (i in cardList!!) {
            adapter.addItem(i, this)
        }

        setUpCardStackView()

        llBottomItems.visible()
        clBottomItems.invisible()

        // Listeners
        cvSettingsOpen.setOnClickListener(onClickBottomItems)
        cvSettingsClosed.setOnClickListener(onClickBottomItems)
        cvEdit.setOnClickListener(onClickBottomItems)
        cvDarkMode.setOnClickListener(onClickBottomItems)
        cvDelete.setOnClickListener(onClickBottomItems)
        btnFlip.setOnClickListener(onClickBottomItems)
    }

    private val onClickBottomItems = View.OnClickListener {

        when (it) {
            cvSettingsOpen -> {
                // When it's opened we click to close it

                llBottomItems.visible()
                clBottomItems.invisible()
            }
            cvSettingsClosed -> {
                // When it's closed we click to open it

                llBottomItems.invisible()
                clBottomItems.visible()
            }
            cvDelete -> {
                adapter.deleteItem(itemModel!!)
                deleteFromFirebase()
            }
            cvEdit -> {

                activity?.toast(stringTest ?: "")

//                val intent = Intent(activity as CardDetailActivity, NewCardActivity::class.java)
//                intent.putExtra("edit", "edit")
//                intent.putExtra("setString", setString)
//                activity!!.startActivity(intent)
            }
            cvDarkMode -> {

                if (sharedPref.getBoolean(DARK_THEME, true)) { //if true now false

                    editor.putBoolean(DARK_THEME, false)

                } else { //if false now true

                    editor.putBoolean(DARK_THEME, true)
                }

                editor.apply()

                val intent = Intent((activity as CardDetailActivity), (activity as CardDetailActivity)::class.java)
                intent.putExtra("setString", setString)
                startActivity(intent)
                activity?.finish()

            }
            btnFlip -> {

                adapter.flipAnimation(this.itemView!!, this.itemModel!!)
            }
        }
    }

    private fun deleteFromFirebase(): Boolean {

        val user = FirebaseAuth.getInstance().currentUser!!
        val userId = user.uid

        val postReference = itemModel?.cardId

        val database = FirebaseDatabase.getInstance()
        val myRef = database.reference.child(userId).child(DATA_CARDS).child(postReference!!)

        myRef.removeValue()
            .addOnSuccessListener {

                activity?.toast("Card deleted")
            }
            .addOnFailureListener {
                activity?.toast("Failure to delete")
            }

        return true

    }

    private fun setUpCardStackView() {
        manager.apply {
            setStackFrom(StackFrom.None)
            setVisibleCount(3)
            setTranslationInterval(8.0f)
            setScaleInterval(0.95f)
            setSwipeThreshold(0.3f)
            setMaxDegree(20.0f)
            setDirections(Direction.HORIZONTAL)
            setCanScrollHorizontal(true)
            setCanScrollVertical(true)
            setSwipeableMethod(SwipeableMethod.AutomaticAndManual)
            setOverlayInterpolator(LinearInterpolator())
        }
        cardStackView.layoutManager = manager
        cardStackView.adapter = adapter
        cardStackView.itemAnimator.apply {
            if (this is DefaultItemAnimator) {
                supportsChangeAnimations = false
            }
        }
    }

    companion object {

        fun newInstance(setString: String): CardDetailFragment {

            val fragment = CardDetailFragment()
            fragment.arguments = Bundle().apply {
                putString("setString", setString)
            }

            return fragment
        }
    }

}
