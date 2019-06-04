package com.francislainy.buffl.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DefaultItemAnimator

import com.francislainy.buffl.R
import com.francislainy.buffl.activities.CardDetailActivity
import com.francislainy.buffl.activities.NewCardActivity
import com.francislainy.buffl.adapter.CardStackAdapter
import com.francislainy.buffl.model.Card
import com.francislainy.buffl.utils.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_card_detail.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.yuyakaido.android.cardstackview.*
import kotlinx.android.synthetic.main.fragment_card_detail.btnFlip
import kotlinx.android.synthetic.main.fragment_card_detail.cardStackView
import kotlinx.android.synthetic.main.fragment_card_detail.clBottomItems
import kotlinx.android.synthetic.main.fragment_card_detail.cvDarkMode
import kotlinx.android.synthetic.main.fragment_card_detail.cvDelete
import kotlinx.android.synthetic.main.fragment_card_detail.cvEdit
import kotlinx.android.synthetic.main.fragment_card_detail.cvSettingsClosed
import kotlinx.android.synthetic.main.fragment_card_detail.cvSettingsOpen
import kotlinx.android.synthetic.main.fragment_card_detail.ivDarkMode
import kotlinx.android.synthetic.main.fragment_card_detail.ivDelete
import kotlinx.android.synthetic.main.fragment_card_detail.ivEdit
import kotlinx.android.synthetic.main.fragment_card_detail.ivNotSure
import kotlinx.android.synthetic.main.fragment_card_detail.ivSettingsClosed
import kotlinx.android.synthetic.main.fragment_card_detail.ivSettingsOpen
import kotlinx.android.synthetic.main.fragment_card_detail.ivStar
import kotlinx.android.synthetic.main.fragment_card_detail.llBottomItems

@SuppressLint("CommitPrefEdits")
class CardDetailFragment : Fragment(), CardStackListener, CardStackAdapter.AdapterCallback {
    override fun onClickCallback(itemModel: Card, itemView: View, position: Int) {
        stringTest = itemModel.cardQuestion
        this.itemModel = itemModel
        this.itemView = itemView
        this.position = position
    }

    override fun onCardDisappeared(view: View?, position: Int) {

//        activity?.toast()
    }

    override fun onCardDragging(direction: Direction?, ratio: Float) {
    }

    override fun onCardSwiped(direction: Direction?) {

        btnFlip.visible()
        clYesNo.invisible()

        when (direction) {
            Direction.Left -> {

            }
            Direction.Right -> {

            }
        }
//        itemView?.tvCardTitle?.performClick()
//
//        adapter.flipAnimation(this.itemView!!, this.itemModel!!)
    }
    override fun onCardCanceled() {}

    override fun onCardAppeared(view: View?, position: Int) {

        onClickCallback(itemModel!!, itemView!!, position)
    }

    override fun onCardRewound() {
    }

    private var objectTitle: String? = null
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

        return inflater.inflate(R.layout.fragment_card_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (sharedPref.getBoolean(DARK_THEME, true)) {
            updateUIDarkMode()
        }

        setString = arguments?.getString("setString")
        objectTitle = arguments?.getString("objectTitle")

        tvSetTitle.text = objectTitle

        /** https://stackoverflow.com/a/9599112/6654475 */
        val collectionType = object : TypeToken<List<Card>>() {}.type
        cardList = Gson().fromJson(setString, collectionType) as List<Card>


        for (i in cardList!!) {
            adapter.addItem(i, this)
        }

        setUpCardStackView()

        llBottomItems.visible()
        clBottomItems.invisible()

        listeners()
    }

    private fun listeners() {
        val listOfClickables = listOf<View>(
            cvSettingsOpen, cvSettingsClosed, cvEdit,
            cvDarkMode, cvDelete, btnFlip
        )

        for (i in listOfClickables) {
            i.setOnClickListener(onClickBottomItems)
        }
    }

    private fun updateUIDarkMode() {

        tvSetTitle.setTvTextColor(R.color.white)
        llParentCardDetail.setBackgroundTint(R.color.blue_dark_theme_exterior)
        cvStar.setBackgroundTint(R.color.blue_dark_theme_inside_image)
        ivStar.setTintImageView(R.color.blue_dark_theme_exterior)
        cvSettingsClosed.setBackgroundTint(R.color.blue_dark_theme_inside_image)
        ivSettingsClosed.setTintImageView(R.color.blue_dark_theme_exterior)

        cvNo.setBackgroundTint(R.color.blue_dark_theme_inside_image)
        cvYes.setBackgroundTint(R.color.blue_dark_theme_inside_image)

        cvDelete.setBackgroundTint(R.color.blue_dark_theme_inside_image)
        ivDelete.setTintImageView(R.color.blue_dark_theme_exterior)
        cvEdit.setBackgroundTint(R.color.blue_dark_theme_inside_image)
        ivEdit.setTintImageView(R.color.blue_dark_theme_exterior)
        cvDarkMode.setBackgroundTint(R.color.blue_dark_theme_inside_image)
        ivDarkMode.setTintImageView(R.color.blue_dark_theme_exterior)
        cvNotSure.setBackgroundTint(R.color.blue_dark_theme_inside_image)
        ivNotSure.setTintImageView(R.color.blue_dark_theme_exterior)
        cvSettingsOpen.setBackgroundTint(R.color.blue_dark_theme_inside_image)
        ivSettingsOpen.setTintImageView(R.color.blue_dark_theme_exterior)
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

                val intent = Intent(activity as CardDetailActivity, NewCardActivity::class.java)
                intent.putExtra("edit", "edit")
                intent.putExtra("setString", setString)
                intent.putExtra("cardString", objectToStringJson(itemModel as Card))
                activity!!.startActivity(intent)
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
                intent.putExtra("objectTitle", objectTitle)
                startActivity(intent)
                activity?.finish()

            }
            btnFlip -> {

                btnFlip.invisible()
                clYesNo.visible()

                onClickCallback(itemModel!!, itemView!!, position!!)

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

        fun newInstance(setString: String, objectTitle: String): CardDetailFragment {

            val fragment = CardDetailFragment()
            fragment.arguments = Bundle().apply {
                putString("setString", setString)
                putString("objectTitle", objectTitle)
            }

            return fragment
        }
    }

}
