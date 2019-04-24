package com.francislainy.buffl.utils

import android.content.Context
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.francislainy.buffl.App
import com.francislainy.buffl.R
import com.francislainy.buffl.fragments.CourseDetailFragment
import com.francislainy.buffl.fragments.CoursesListFragment

fun RecyclerView.setVerticalLayout() {

    val layoutManager = LinearLayoutManager(App.context)
    layoutManager.orientation = RecyclerView.VERTICAL
    this.layoutManager = layoutManager
    this.isNestedScrollingEnabled = false
}

fun Context.toast(message: CharSequence = "toast") = Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> Unit) {
    val fragmentTransaction = beginTransaction()
    fragmentTransaction.func()
    fragmentTransaction.commit()
}

fun AppCompatActivity.addFragment(fragment: Fragment, frameId: Int, backStackTag: String? = null) {
    supportFragmentManager.inTransaction {
        add(frameId, fragment)
        backStackTag?.let { addToBackStack(fragment.javaClass.name) }
    }
}

fun AppCompatActivity.replaceFragment(fragment: Fragment, frameId: Int, backStackTag: String? = null) {
    supportFragmentManager.inTransaction {
        replace(frameId, fragment)
        backStackTag?.let { addToBackStack(fragment.javaClass.name) }
    }
}