package com.prathamngundikere.socialmedia

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class PhotoFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PhotoFeedAdapter
    private val photoList = mutableListOf<String>()
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_photos, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = view.findViewById(R.id.photoFeedRecycler)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = PhotoFeedAdapter(photoList)
        recyclerView.adapter = adapter

        listenForPhotos()
    }

    private fun listenForPhotos() {
        db.collection("photos")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, e ->
                if (e != null) return@addSnapshotListener
                if (snapshot != null && !snapshot.isEmpty) {
                    photoList.clear()
                    photoList.addAll(snapshot.documents.mapNotNull { it.getString("url") })
                    adapter.notifyDataSetChanged()
                }
            }
    }
}
