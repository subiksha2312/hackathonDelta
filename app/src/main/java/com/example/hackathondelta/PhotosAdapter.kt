package com.example.hackathondelta

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.photodetails.view.*

class PhotosAdapter(private val photosList: List<Photos>,
                    private val onItemClicked: (position: Int) -> Unit): RecyclerView.Adapter<PhotosAdapter.ViewHolder> ()
{

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotosAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.photodetails,parent,false)
        return ViewHolder(view, onItemClicked)
    }

    override fun onBindViewHolder(holder: PhotosAdapter.ViewHolder, position: Int) {
        holder.bindPhotos(photosList[position])

    }

    override fun getItemCount(): Int {
        return photosList.size

    }

    class ViewHolder(val view: View, private val onItemClicked: (position: Int) -> Unit):
        RecyclerView.ViewHolder(view), View.OnClickListener{

            init {
                itemView.setOnClickListener(this)
            }

        fun bindPhotos(photos: Photos) {
            itemView.theImage.setImageURI(photos.photoUri)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            onItemClicked(position)
        }
    }

}