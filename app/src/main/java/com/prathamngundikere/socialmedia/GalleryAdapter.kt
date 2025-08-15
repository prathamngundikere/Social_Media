package com.prathamngundikere.socialmedia

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class GalleryAdapter(
    private val images: List<GalleryImage>,
    private val onImageClick: (Uri) -> Unit
) : RecyclerView.Adapter<GalleryAdapter.GalleryViewHolder>() {

    class GalleryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.galleryImageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_gallery_image, parent, false)
        return GalleryViewHolder(view)
    }

    override fun onBindViewHolder(holder: GalleryViewHolder, position: Int) {
        val imageUri = images[position].uri
        Glide.with(holder.imageView.context)
            .load(imageUri)
            .centerCrop()
            .into(holder.imageView)

        holder.imageView.setOnClickListener {
            onImageClick(imageUri)
        }
    }

    override fun getItemCount() = images.size
}
