package com.dicoding.mystories.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.mystories.data.response.ListStoryItem
import com.dicoding.mystories.databinding.ItemCardStoryBinding

class StoryListAdapter : PagingDataAdapter<ListStoryItem, StoryListAdapter.MyViewHolder>(
    DIFF_CALLBACK
) {
    interface OnItemClickCallback {
        fun onItemClicked(image: View, name: View, desc: View, id: String)
    }

    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemCardStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val story = getItem(position)
        if (story != null) {
            holder.bind(story)

            holder.binding.imageView.transitionName = "story_image_${story.id}"
            holder.binding.name.transitionName = "story_name_${story.id}"
            holder.binding.description.transitionName = "story_description_${story.id}"

            holder.itemView.setOnClickListener {
                story.id?.let { id ->
                    onItemClickCallback?.onItemClicked(holder.binding.imageView, holder.binding.name, holder.binding.description, id)
                }
            }
        }
    }

    class MyViewHolder(val binding: ItemCardStoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(story: ListStoryItem) {
            binding.name.text = story.name
            binding.description.text = story.description
            Glide.with(binding.root)
                .load(story.photoUrl)
                .into(binding.imageView)
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: ListStoryItem,
                newItem: ListStoryItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}