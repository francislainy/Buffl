package com.francislainy.buffl.fragments.drawer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.francislainy.buffl.R
import com.francislainy.buffl.model.Course
import com.francislainy.buffl.utils.DATA_COURSES
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.fragment_drawer.*
import kotlinx.android.synthetic.main.row_drawer_item.view.*
import timber.log.Timber

/** https://guides.codepath.com/android/Fragment-Navigation-Drawer#navigating-between-menu-items */
class FragmentDrawer : Fragment() {

    private var drawerToggle: ActionBarDrawerToggle? = null
    private var drawerLayout: DrawerLayout? = null
    private var containerView: View? = null
    private var drawerListener: FragmentDrawerListener? = null
    private lateinit var adapter: GroupAdapter<ViewHolder>

    fun openDrawer() {
        drawerLayout?.openDrawer(containerView!!)
    }

    fun setDrawerListener(listener: FragmentDrawerListener) {
        this.drawerListener = listener
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_drawer, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = GroupAdapter()
        rvDrawerItems.adapter = adapter
        fetchCourses(adapter)
    }

    fun closeNavDrawer() {
        drawerLayout!!.closeDrawer(containerView!!)
    }

    fun isNavDrawerOpen(): Boolean {
        return drawerLayout!!.isDrawerOpen(GravityCompat.START)
    }

    fun setUp(fragmentId: Int, drawerLayout: DrawerLayout, toolbar: Toolbar) {
        containerView = activity!!.findViewById(fragmentId)
        this.drawerLayout = drawerLayout
        drawerToggle = object :
            ActionBarDrawerToggle(activity, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            override fun onDrawerOpened(drawerView: View) {
                super.onDrawerOpened(drawerView)
                activity!!.invalidateOptionsMenu()
            }

            override fun onDrawerClosed(drawerView: View) {
                super.onDrawerClosed(drawerView)
                activity!!.invalidateOptionsMenu()
            }

            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                super.onDrawerSlide(drawerView, slideOffset)
                toolbar.alpha = 1 - slideOffset / 2
            }
        }

        this.drawerLayout!!.setDrawerListener(drawerToggle)
        this.drawerLayout!!.post { drawerToggle!!.syncState() }
    }

    private fun fetchCourses(adapter: GroupAdapter<ViewHolder>) {
        val user = FirebaseAuth.getInstance().currentUser!!
        val userId = user.uid

        val database = FirebaseDatabase.getInstance()
        val myRef = database.reference.child(userId).child(DATA_COURSES)
        myRef.orderByKey()

        // Read from the database
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                adapter.clear()

                dataSnapshot.children.forEach {

                    val cardMap = it.value
                    val json = Gson().toJson(cardMap)
                    val course = Gson().fromJson<Course>(json, Course::class.java)

                    adapter.add(CourseItem(course))
                }

            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Timber.w("Failed to read value. + ${error.toException()}")
            }
        })
    }

    class CourseItem(private val c: Course) : Item<ViewHolder>() {

        override fun bind(viewHolder: ViewHolder, position: Int) {

            with(viewHolder.itemView) {

                tvItemTitle.text = c.courseTitle
            }

        }

        override fun getLayout() = R.layout.row_drawer_item

    }

    interface FragmentDrawerListener {
        fun onDrawerItemSelected(view: View, position: Int)
    }

}