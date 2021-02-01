package com.fasila.aqsalandmarks.ui.stages.badges

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.fasila.aqsalandmarks.databinding.BadgeItemBinding
import com.fasila.aqsalandmarks.model.badge.Badge

class BadgesRecyclerViewAdapter : ListAdapter<Badge, BadgesRecyclerViewAdapter.BadgesViewHolder>(BadgeDiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BadgesViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = BadgeItemBinding.inflate(layoutInflater, parent, false)
        return BadgesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BadgesViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class BadgesViewHolder(private val binding: BadgeItemBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(item: Badge) {
            binding.badge = item
        }
    }
}

class BadgeDiffCallBack : DiffUtil.ItemCallback<Badge>(){
    override fun areItemsTheSame(oldItem: Badge, newItem: Badge): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Badge, newItem: Badge): Boolean {
        return oldItem == newItem
    }

}