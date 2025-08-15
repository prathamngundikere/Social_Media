package com.prathamngundikere.socialmedia

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class PhotoFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_photos, container, false)
        val recycler = view.findViewById<RecyclerView>(R.id.photoRecycler)

        // Sample list for UI
        val items = List(50) { MyItem("Photo $it", "Subtitle $it") }
        recycler.layoutManager = LinearLayoutManager(requireContext())
        recycler.adapter = MyAdapter(items)

        // Hide/show bottom nav on scroll
        recycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val activity = requireActivity() as MainActivity
                if (dy > 0) {
                    activity.hideBottomNav() // scrolling down
                } else if (dy < 0) {
                    activity.showBottomNav() // scrolling up
                }
            }
        })

        return view
    }
}