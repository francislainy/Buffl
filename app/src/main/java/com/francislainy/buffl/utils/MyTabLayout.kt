package com.francislainy.buffl.utils

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.Gravity
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.widget.TextViewCompat
import androidx.viewpager.widget.ViewPager
import com.francislainy.buffl.R
import com.google.android.material.tabs.TabLayout

/** https://medium.com/@pjonceski/custom-views-at-tablayout-with-highlighted-text-style-at-the-selected-tab-7cb6389a633 */

class MyTabLayout : TabLayout {
    private val titles: MutableList<String> = arrayListOf()
    private var unselectedTypeFace: Typeface? = null

    constructor(context: Context?) : super(context) {
        init(context, null)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    private fun init(context: Context?, attrs: AttributeSet?) {
        addOnTabSelectedListener()
    }

    private fun createCustomFontTextViewForTab(): AppCompatTextView {
        val customFontTextView = AppCompatTextView(context)
        customFontTextView.gravity = Gravity.CENTER
        TextViewCompat.setTextAppearance(customFontTextView, R.style.MyTitleStyle)
        return customFontTextView
    }

    private fun addViews() {
        for (i in 0 until tabCount) {
            val tab = getTabAt(i)
            if (tab != null) {
                val customFontTextView = createCustomFontTextViewForTab()
                if (i == 0) {
                    if (unselectedTypeFace == null) {
                        unselectedTypeFace = customFontTextView.typeface
                    }
                    customFontTextView.setTypeface(customFontTextView.typeface, Typeface.BOLD)
                }
                tab.customView = customFontTextView
            }
        }
    }

    private fun addOnTabSelectedListener() {
        addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: Tab?) {
            }

            override fun onTabUnselected(tab: Tab?) {
                val view = tab?.customView
                if (view is AppCompatTextView) {
                    view.typeface = unselectedTypeFace
                }
            }

            override fun onTabSelected(tab: Tab?) {
                val view = tab?.customView
                if (view is AppCompatTextView) {
                    view.setTypeface(view.typeface, Typeface.BOLD)
                }
            }
        })
    }

    private fun setTitlesAtTabs(titles: List<String>?) {
        if (titles == null || titles.size < tabCount) {
            return
        }
        if (this.titles.size > 0) {
            this.titles.clear()
        }
        this.titles.addAll(titles)
        for (i in 0 until tabCount) {
            val tab = getTabAt(i)
            if (tab != null) {
                val view = tab.customView
                if (view is AppCompatTextView) {
                    view.text = this.titles[i]
                }
            }
        }
    }

    override fun setupWithViewPager(viewPager: ViewPager?, autoRefresh: Boolean) {
        super.setupWithViewPager(viewPager, autoRefresh)
        addViews()
        setTitlesAtTabs(listOf("Learn", "Explore", "new"))
    }
}