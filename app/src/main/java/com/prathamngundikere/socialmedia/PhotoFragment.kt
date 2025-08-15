package com.prathamngundikere.socialmedia

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class PhotoFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var photoAdapter: PhotoAdapter
    private val photoList = mutableListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_photos, container, false)
        recyclerView = view.findViewById(R.id.photoRecyclerView)
        recyclerView.layoutManager = GridLayoutManager(context, 2)
        photoAdapter = PhotoAdapter(photoList)
        recyclerView.adapter = photoAdapter

        FirebaseFirestore.getInstance().collection("photos")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { value, _ ->
                if (value != null) {
                    photoList.clear()
                    for (doc in value) {
                        photoList.add(doc.getString("url") ?: "")
                    }
                    photoAdapter.notifyDataSetChanged()
                }
            }

        return view
    }
}
