package com.mdp.tourisview.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.mdp.tourisview.R
import com.mdp.tourisview.data.local.room.model.RoomDestination
import com.mdp.tourisview.databinding.DestinationItemBinding
import com.squareup.picasso.Picasso

class DestinationAdapter(
    private val action: Action
): ListAdapter<RoomDestination, DestinationAdapter.DestinationAdapterViewHolder>(
    DIFF_CALLBACK
) {
    inner class DestinationAdapterViewHolder(
        private val binding: DestinationItemBinding
    ): RecyclerView.ViewHolder(binding.root){
        fun bind(roomDestination: RoomDestination){
            binding.imageView.setImageResource(R.drawable.image_24px)
            binding.nameTv.text = roomDestination.name
            binding.locationTv.text = roomDestination.locationName
            binding.root.setOnClickListener { action.onClick(roomDestination) }
            Picasso.get().load(roomDestination.imageUrl)
                .placeholder(R.drawable.image_placeholder)
                .error(R.drawable.image_placeholder)
                .into(binding.imageView)
            Log.d("Tes toggle", "Adapter boolean: ${roomDestination.isBookmarked}")
            if(roomDestination.isBookmarked){
                (binding.bookmarkButton as MaterialButton).setIconResource(R.drawable.bookmark_filled_24px)
            }else{
                (binding.bookmarkButton as MaterialButton).setIconResource(R.drawable.bookmark_24px)
            }
            binding.bookmarkButton.setOnClickListener {
                action.bookmarkClick(roomDestination)
            }
            binding.starTv.text = roomDestination.avgStar.toString()
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DestinationAdapterViewHolder {
        val binding = DestinationItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DestinationAdapterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DestinationAdapterViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object{
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<RoomDestination>(){
            override fun areItemsTheSame(oldItem: RoomDestination, newItem: RoomDestination): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: RoomDestination, newItem: RoomDestination): Boolean {
                return oldItem == newItem
            }

        }
    }

    interface Action{
        fun onClick(roomDestination: RoomDestination)
        fun bookmarkClick(roomDestination: RoomDestination)
    }
}