package com.francislainy.buffl.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.francislainy.buffl.R


class NewSetFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_new_set, container, false)
    }

    companion object {
        fun newInstance(courseString: String) =
            NewSetFragment().apply {
                arguments = Bundle().apply {
                    //                    putString("courseString", courseString)
                }
            }
    }
}
