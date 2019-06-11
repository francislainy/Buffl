package com.francislainy.buffl.utils

import android.content.Context
import android.content.res.ColorStateList
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.francislainy.buffl.App
import com.francislainy.buffl.R
import com.francislainy.buffl.fragments.SetListFragment
import kotlinx.android.synthetic.main.fragment_card_detail.*

fun RecyclerView.setVerticalLayout() {

    val layoutManager = LinearLayoutManager(App.context)
    layoutManager.orientation = RecyclerView.VERTICAL
    this.layoutManager = layoutManager
    this.isNestedScrollingEnabled = false
}

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun Context.toast(message: CharSequence = "toast") = Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

fun TextView.setTvTextColor(colorId: Int) {
    this.setTextColor(ContextCompat.getColor(context, colorId))
}

fun ImageView.setTintImageView(colorId: Int) {
    this.setColorFilter(ContextCompat.getColor(context, colorId), android.graphics.PorterDuff.Mode.SRC_IN)
}

fun View.setBackgroundTint(colorId: Int) {
    this.backgroundTintList = ColorStateList.valueOf(resources.getColor(colorId))
}

fun View.setBackgroundColorExt(colorId: Int) {
    this.setBackgroundColor(ContextCompat.getColor(context, colorId))
}

inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> Unit) {
    val fragmentTransaction = beginTransaction()
    fragmentTransaction.func()
    fragmentTransaction.commit()
}

fun AppCompatActivity.addFragment(fragment: Fragment, frameId: Int) {

    supportFragmentManager.beginTransaction()
        .add(frameId, fragment).commit()

}

fun AppCompatActivity.replaceFragment(fragment: Fragment, frameId: Int, backStackTag: String? = null) {
    supportFragmentManager.inTransaction {
        replace(frameId, fragment)
        backStackTag?.let { addToBackStack(fragment.javaClass.name) }
    }
}