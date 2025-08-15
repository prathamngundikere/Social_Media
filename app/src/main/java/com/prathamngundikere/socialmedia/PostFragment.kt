package com.prathamngundikere.socialmedia

import android.Manifest
import android.app.Activity
import android.content.ContentUris
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class PostFragment : Fragment() {

    private lateinit var selectedImageView: ImageView
    private lateinit var chooseImageButton: Button
    private lateinit var uploadImageButton: Button
    private lateinit var uploadProgressBar: ProgressBar
    private var imageUri: Uri? = null

    private val PICK_IMAGE_REQUEST = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_post, container, false)

        selectedImageView = view.findViewById(R.id.selectedImageView)
        chooseImageButton = view.findViewById(R.id.chooseImageButton)
        uploadImageButton = view.findViewById(R.id.uploadImageButton)
        uploadProgressBar = view.findViewById(R.id.uploadProgressBar)

        chooseImageButton.setOnClickListener { openFileChooser() }
        uploadImageButton.setOnClickListener { uploadImage() }

        return view
    }

    private fun openFileChooser() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            imageUri = data.data
            selectedImageView.setImageURI(imageUri)
        }
    }

    private fun uploadImage() {
        if (imageUri != null) {
            val fileRef = FirebaseStorage.getInstance()
                .reference.child("uploads/${System.currentTimeMillis()}.jpg")

            uploadProgressBar.visibility = View.VISIBLE

            fileRef.putFile(imageUri!!)
                .addOnProgressListener { taskSnapshot ->
                    val progress = (100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount).toInt()
                    uploadProgressBar.progress = progress
                }
                .addOnSuccessListener {
                    fileRef.downloadUrl.addOnSuccessListener { uri ->
                        val photoData = hashMapOf(
                            "url" to uri.toString(),
                            "timestamp" to System.currentTimeMillis()
                        )
                        FirebaseFirestore.getInstance().collection("photos").add(photoData)
                        Toast.makeText(context, "Upload successful", Toast.LENGTH_SHORT).show()
                        uploadProgressBar.visibility = View.GONE
                        uploadProgressBar.progress = 0
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(context, "Upload failed", Toast.LENGTH_SHORT).show()
                    uploadProgressBar.visibility = View.GONE
                }
        } else {
            Toast.makeText(context, "No file selected", Toast.LENGTH_SHORT).show()
        }
    }
}