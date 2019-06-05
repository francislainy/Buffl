package com.francislainy.buffl.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.francislainy.buffl.R
import kotlinx.android.synthetic.main.fragment_new_card.*
import com.francislainy.buffl.model.Card
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import com.francislainy.buffl.model.MySet
import com.francislainy.buffl.utils.*
import android.view.inputmethod.InputMethodManager.SHOW_IMPLICIT
import android.content.Context.INPUT_METHOD_SERVICE
import android.view.inputmethod.InputMethodManager

private const val BOX_ONE = 1

class NewCardFragment : Fragment(), Toolbar.OnMenuItemClickListener {

    private var card: Card? = null
    private var edit: String? = null
    private var question: String? = null
    private var answer: String? = null
    private var mySet: MySet? = null
    private var cardList: List<Card>? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_new_card, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val setString = arguments?.getString("setString")
        edit = arguments?.getString("edit")
        val cardString = arguments?.getString("cardString")

        if (edit == null) { // When creating a new card
            mySet = objectFromJsonString(setString, MySet::class.java)
        } else { // When coming from the edit state we're bringing a collection instead of a set
//            val collectionType = object : TypeToken<List<Card>>() {}.type
//            cardList = Gson().fromJson(setString, collectionType) as List<Card>
            card = objectFromJsonString(cardString, Card::class.java)

            etQuestion.setText(card!!.cardQuestion)
            etAnswer.setText(card!!.cardAnswer)
        }

        val toolbar = activity!!.findViewById(R.id.toolbar) as Toolbar
        toolbar.inflateMenu(R.menu.main_menu)
        toolbar.setOnMenuItemClickListener(this)

        // Listeners
        btnFront.setOnClickListener(onClickFrontBackButton)
        btnBack.setOnClickListener(onClickFrontBackButton)
    }

    private val onClickFrontBackButton = View.OnClickListener { view ->

        when (view?.id) {
            R.id.btnFront -> {

                etQuestion.visible()
                etAnswer.invisible()

                etQuestion.requestFocus()
                val imm = activity?.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager?
                imm!!.showSoftInput(etQuestion, SHOW_IMPLICIT)

                btnFront.setTvTextColor(R.color.black)
                btnBack.setTvTextColor(R.color.dark_grey_aaa)
                btnFront.setBackgroundResource(R.drawable.shape_rectangle_bottom_border_green)
                btnBack.setBackgroundResource(R.drawable.shape_rectangle_bottom_border_same_grey)

                question = etQuestion.text?.toString()
            }
            R.id.btnBack -> {

                etQuestion.invisible()
                etAnswer.visible()

                // Have the editText ready for accepting typping
                etAnswer.requestFocus()
                val imm = activity?.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager?
                imm!!.showSoftInput(etAnswer, SHOW_IMPLICIT)

                btnBack.setTvTextColor(R.color.black)
                btnFront.setTvTextColor(R.color.dark_grey_aaa)
                btnBack.setBackgroundResource(R.drawable.shape_rectangle_bottom_border_green)
                btnFront.setBackgroundResource(R.drawable.shape_rectangle_bottom_border_same_grey)

                answer = etAnswer.text?.toString()
            }
        }

    }

    private fun saveToFirebase(cardQuestion: String, cardAnswer: String): Boolean {

        val user = FirebaseAuth.getInstance().currentUser!!
        val userId = user.uid

        val database = FirebaseDatabase.getInstance()
        val myRef = database.reference.child(userId).child(DATA_CARDS)

        if (cardQuestion.isEmpty() || cardQuestion.isEmpty()) {
            return false
        }

        val newPostReference = myRef.push()

        val card = Card(
            newPostReference.key!!,
            mySet!!.setId,
            cardQuestion,
            cardAnswer, BOX_ONE
        )

        newPostReference.setValue(card)
            .addOnSuccessListener {

                activity?.toast("card saved")
                activity?.finish()
            }
            .addOnFailureListener {
                activity?.toast("failure")
            }

        return true
    }

    private fun updateToFirebase(cardQuestion: String, cardAnswer: String): Boolean {

        if (cardQuestion.isEmpty() || cardQuestion.isEmpty()) {
            return false
        }

        val user = FirebaseAuth.getInstance().currentUser!!
        val userId = user.uid

        val database = FirebaseDatabase.getInstance()
        val myRef = database.reference.child(userId).child(DATA_CARDS).child(card!!.cardId)


        if (cardQuestion != card!!.cardQuestion) { //check as if the same value as before don't need to do an update

            myRef.child("cardQuestion").setValue(cardQuestion)
                .addOnSuccessListener {

                    activity?.toast("card updated")
                    activity?.finish()
                }
                .addOnFailureListener {
                    activity?.toast("failure")
                }
        }

        if (cardAnswer != card!!.cardAnswer) {

            myRef.child("cardAnswer").setValue(cardAnswer)
                .addOnSuccessListener {

                    activity?.toast("card updated")
                    activity?.finish()
                }
                .addOnFailureListener {
                    activity?.toast("failure")
                }
        }

        return true
    }

    override fun onMenuItemClick(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.menu_check -> {

                question = etQuestion.text?.toString()
                answer = etAnswer.text?.toString()

                if (question.isNullOrEmpty()) {
                    activity?.toast("Question can not be empty")
                    return false
                } else if (answer.isNullOrEmpty()) {
                    activity?.toast("Answer can not be empty")
                    return false
                }

                if (edit == null) {

                    saveToFirebase(question!!, answer!!)
                } else {
                    updateToFirebase(question!!, answer!!)
                }
                return true
            }
        }
        return false
    }

    companion object {

        fun newInstance(setString: String, edit: String? = null, cardString: String? = null): NewCardFragment {

            val fragment = NewCardFragment()
            fragment.arguments = Bundle().apply {
                putString("setString", setString)
                putString("edit", edit)
                putString("cardString", cardString)
            }

            return fragment
        }
    }
}