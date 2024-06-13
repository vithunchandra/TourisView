package com.mdp.tourisview.adapter

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.mdp.tourisview.R
import com.mdp.tourisview.data.local.room.model.RoomDestination
import com.mdp.tourisview.data.mock.server.model.MockServerReview
import com.mdp.tourisview.databinding.DestinationItemBinding
import com.mdp.tourisview.databinding.ReviewItemBinding
import com.squareup.picasso.Picasso

class ReviewAdapter() : ListAdapter<MockServerReview, ReviewAdapter.ReviewAdapterViewHolder>(
    DIFF_CALLBACK
) {
    inner class ReviewAdapterViewHolder(
        private val binding: ReviewItemBinding
    ): RecyclerView.ViewHolder(binding.root){
        fun bind(review: MockServerReview){
            binding.reviewItemReviewer.text = review.reviewer
            val star = listOf(binding.reviewItemStar1, binding.reviewItemStar2, binding.reviewItemStar3, binding.reviewItemStar4, binding.reviewItemStar5)
            for (i in 0..(star.size-1)){
                if (i < review.star){
                    star.get(i).isVisible = true
                }else{
                    star.get(i).isVisible = false
                }
            }
            binding.reviewItemText.text = review.review
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ReviewAdapter.ReviewAdapterViewHolder {
        val binding = ReviewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReviewAdapterViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ReviewAdapter.ReviewAdapterViewHolder,
        position: Int
    ) {
        holder.bind(getItem(position))
    }

    companion object{
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<MockServerReview>(){
            override fun areItemsTheSame(oldItem: MockServerReview, newItem: MockServerReview): Boolean {
                return oldItem.review_id == newItem.review_id
            }

            override fun areContentsTheSame(oldItem: MockServerReview, newItem: MockServerReview): Boolean {
                return oldItem == newItem
            }

        }
    }
}