package com.mdp.tourisview.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.mdp.tourisview.R
import com.mdp.tourisview.data.local.room.model.RoomDestination
import com.mdp.tourisview.databinding.BookmarkedDestinationItemBinding
import com.squareup.picasso.Picasso

class BookmarkedDestinationAdapter(
    private val context: Context,
    private val action: Action
): ListAdapter<RoomDestination, BookmarkedDestinationAdapter.BookmarkedDestinationAdapterViewHolder>(
    DIFF_CALLBACK
) {
    inner class BookmarkedDestinationAdapterViewHolder(
        val binding: BookmarkedDestinationItemBinding
    ): RecyclerView.ViewHolder(binding.root){
        fun bind(item: RoomDestination){
            Picasso.get()
                .load(item.imageUrl)
                .into(binding.destinationIv)
            binding.nameTv.text = item.name
            binding.locationTv.text = item.locationName
            if(item.isBookmarked){
                (binding.bookmarkButton as MaterialButton).setIconResource(R.drawable.bookmark_filled_24px)
            }else{
                (binding.bookmarkButton as MaterialButton).setIconResource(R.drawable.bookmark_24px)
            }
            binding.bookmarkButton.setOnClickListener {
                action.bookmarkClicked(item)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BookmarkedDestinationAdapterViewHolder {
        val binding = BookmarkedDestinationItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BookmarkedDestinationAdapterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BookmarkedDestinationAdapterViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object{
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<RoomDestination>(){
            override fun areItemsTheSame(oldItem: RoomDestination, newItem: RoomDestination): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: RoomDestination, newItem: RoomDestination): Boolean {
                return oldItem == newItem
            }

        }
    }

    interface Action{
        fun bookmarkClicked(destination: RoomDestination)
    }
}