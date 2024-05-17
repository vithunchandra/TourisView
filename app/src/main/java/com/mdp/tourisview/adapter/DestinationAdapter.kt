package com.mdp.tourisview.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.model.LatLng
import com.mdp.tourisview.data.mock.model.Destination
import com.mdp.tourisview.databinding.DestinationItemBinding
import com.mdp.tourisview.util.getAddress

class DestinationAdapter(
    private val context: Context,
    private val action: Action
): ListAdapter<Destination, DestinationAdapter.DestinationAdapterViewHolder>(
    DIFF_CALLBACK
) {
    inner class DestinationAdapterViewHolder(
        private val binding: DestinationItemBinding
    ): RecyclerView.ViewHolder(binding.root){
        fun bind(destination: Destination){
            val location = getAddress(
                LatLng(destination.latitude, destination.longitude),
                context
            )
            binding.imageView.setImageURI(destination.imageUri)
            binding.nameTv.text = destination.name
            binding.locationTv.text = location
            binding.root.setOnClickListener { action.onClick(destination) }
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
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Destination>(){
            override fun areItemsTheSame(oldItem: Destination, newItem: Destination): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Destination, newItem: Destination): Boolean {
                return oldItem == newItem
            }

        }
    }

    interface Action{
        fun onClick(destination: Destination)
    }
}