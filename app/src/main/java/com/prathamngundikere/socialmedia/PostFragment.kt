package com.prathamngundikere.socialmedia

import android.Manifest
import android.content.ContentUris
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class PostFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: GalleryAdapter
    private val images = mutableListOf<GalleryImage>()

    private val storageRef = FirebaseStorage.getInstance().reference
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_post, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = view.findViewById(R.id.galleryRecycler)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 3)

        checkPermissionsAndLoad()
    }

    private fun checkPermissionsAndLoad() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(arrayOf(Manifest.permission.READ_MEDIA_IMAGES), 101)
        } else {
            requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 101)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 101 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            loadGalleryImages()
        } else {
            Toast.makeText(requireContext(), "Permission required to select photos", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadGalleryImages() {
        val collection = mutableListOf<GalleryImage>()
        val uriExternal: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(MediaStore.Images.Media._ID)

        val cursor = requireContext().contentResolver.query(
            uriExternal,
            projection,
            null,
            null,
            MediaStore.Images.Media.DATE_ADDED + " DESC"
        )

        cursor?.use {
            val idColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            while (it.moveToNext()) {
                val id = it.getLong(idColumn)
                val contentUri = ContentUris.withAppendedId(uriExternal, id)
                collection.add(GalleryImage(contentUri))
            }
        }

        images.clear()
        images.addAll(collection)

        adapter = GalleryAdapter(images) { selectedUri ->
            uploadPhoto(selectedUri)
        }
        recyclerView.adapter = adapter
    }

    private fun uploadPhoto(uri: Uri) {
        val fileName = "photos/${System.currentTimeMillis()}.jpg"
        val photoRef = storageRef.child(fileName)

        photoRef.putFile(uri)
            .addOnSuccessListener {
                photoRef.downloadUrl.addOnSuccessListener { downloadUri ->
                    val photoData = mapOf(
                        "url" to downloadUri.toString(),
                        "timestamp" to System.currentTimeMillis()
                    )
                    db.collection("photos").add(photoData)
                        .addOnSuccessListener {
                            Toast.makeText(requireContext(), "Photo Uploaded", Toast.LENGTH_SHORT).show()
                        }
                }
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Upload Failed", Toast.LENGTH_SHORT).show()
            }
    }
}

