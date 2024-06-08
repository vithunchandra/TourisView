package com.mdp.tourisview.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.mdp.tourisview.R
import com.mdp.tourisview.data.mock.server.model.MockServerDestination
import com.mdp.tourisview.databinding.DestinationItemBinding
import com.squareup.picasso.Picasso

class HistoryDestinationAdapter(
    private val action: Action
): ListAdapter<MockServerDestination, HistoryDestinationAdapter.HistoryDestinationAdapterViewHolder>(
    DIFF_CALLBACK
) {
    inner class HistoryDestinationAdapterViewHolder(
        private val binding: DestinationItemBinding
    ): RecyclerView.ViewHolder(binding.root){
        fun bind(mockServerDestination: MockServerDestination){
            binding.imageView.setImageResource(R.drawable.image_24px)
            binding.nameTv.text = mockServerDestination.name
            binding.locationTv.text = mockServerDestination.locationName
            binding.root.setOnClickListener { action.onClick(mockServerDestination) }
            Picasso.get().load(mockServerDestination.imageUrl)
                .placeholder(R.drawable.image_placeholder)
                .error(R.drawable.image_placeholder)
                .into(binding.imageView)
            Log.d("Tes toggle", "Adapter boolean: ${mockServerDestination.isBookmarked}")
            if(mockServerDestination.isBookmarked){
                (binding.bookmarkButton as MaterialButton).setIconResource(R.drawable.bookmark_filled_24px)
            }else{
                (binding.bookmarkButton as MaterialButton).setIconResource(R.drawable.bookmark_24px)
            }
            binding.bookmarkButton.setOnClickListener {
                action.bookmarkClick(mockServerDestination)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HistoryDestinationAdapterViewHolder {
        val binding = DestinationItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryDestinationAdapterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryDestinationAdapterViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object{
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<MockServerDestination>(){
            override fun areItemsTheSame(oldItem: MockServerDestination, newItem: MockServerDestination): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: MockServerDestination, newItem: MockServerDestination): Boolean {
                return oldItem == newItem
            }

        }
    }

    interface Action{
        fun onClick(mockServerDestination: MockServerDestination)
        fun bookmarkClick(mockServerDestination: MockServerDestination)
    }
}