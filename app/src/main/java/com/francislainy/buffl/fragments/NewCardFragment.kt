package com.francislainy.buffl.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.francislainy.buffl.R
import com.francislainy.buffl.utils.toast
import kotlinx.android.synthetic.main.fragment_new_card.*
import com.francislainy.buffl.model.Card
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import com.francislainy.buffl.model.Course
import com.francislainy.buffl.model.MySet
import com.francislainy.buffl.utils.DATA_CARDS
import com.francislainy.buffl.utils.objectFromJsonString

private const val BOX_ONE = 1

class NewCardFragment : Fragment(), Toolbar.OnMenuItemClickListener {

    private var question: String? = null
    private var answer: String? = null
    private var mySet: MySet? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_new_card, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val setString = arguments?.getString("setString")
        mySet = objectFromJsonString(setString, MySet::class.java)

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

                etQuestion.visibility = View.VISIBLE
                etAnswer.visibility = View.INVISIBLE

                btnFront.setTextColor(resources.getColor(R.color.black))
                btnBack.setTextColor(resources.getColor(R.color.dark_grey_aaa))
                btnFront.setBackgroundResource(R.drawable.shape_rectangle_bottom_border_green)
                btnBack.setBackgroundResource(R.drawable.shape_rectangle_bottom_border_same_grey)

                question = etQuestion.text?.toString()
            }
            R.id.btnBack -> {

                etQuestion.visibility = View.INVISIBLE
                etAnswer.visibility = View.VISIBLE

                btnBack.setTextColor(resources.getColor(R.color.black))
                btnFront.setTextColor(resources.getColor(R.color.dark_grey_aaa))
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

        val card = Card(
            mySet!!.setId,
            cardQuestion,
            cardAnswer, BOX_ONE
        )

        myRef.push().setValue(card)
            .addOnSuccessListener {

                activity?.toast("card saved")
                activity?.finish()
            }
            .addOnFailureListener {
                activity?.toast("failure")
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

                saveToFirebase(question!!, answer!!)
                return true
            }
        }
        return false
    }

    companion object {

        fun newInstance(setString: String): NewCardFragment {

            val fragment = NewCardFragment()
            fragment.arguments = Bundle().apply {
                putString("setString", setString)
            }

            return fragment
        }
    }
}