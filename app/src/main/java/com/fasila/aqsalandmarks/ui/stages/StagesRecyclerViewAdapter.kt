package com.fasila.aqsalandmarks.ui.stages

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.fasila.aqsalandmarks.databinding.StageItemBinding
import com.fasila.aqsalandmarks.model.stage.Stage

class StagesRecyclerViewAdapter (private val clickListener: StageListener) : ListAdapter <Stage, StagesRecyclerViewAdapter.StageViewHolder>(StageDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StageViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = StageItemBinding.inflate(layoutInflater, parent, false)
        return StageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StageViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item,
            //stagesList.value!![position],
            clickListener)
    }


    class StageViewHolder(private val binding: StageItemBinding) : RecyclerView.ViewHolder (binding.root) {

        fun bind(item: Stage, clickListener: StageListener) {
            binding.clickListener = clickListener
            binding.stage = item
        }
    }
}

class StageDiffCallback : DiffUtil.ItemCallback<Stage>() {
    override fun areItemsTheSame(oldItem: Stage, newItem: Stage): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Stage, newItem: Stage): Boolean {
        return oldItem == newItem
    }

}