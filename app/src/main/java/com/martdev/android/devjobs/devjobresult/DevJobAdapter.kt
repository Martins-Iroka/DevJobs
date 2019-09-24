package com.martdev.android.devjobs.devjobresult

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.martdev.android.devjobs.databinding.DevjobItemViewBinding
import com.martdev.android.devjobs.network.DevJob

class DevJobAdapter(private val onClickListener: OnClickListener)
    : ListAdapter<DevJob, DevJobAdapter.DevJobViewHolder>(DiffCallback) {

    class DevJobViewHolder(private var binding: DevjobItemViewBinding) :
            RecyclerView.ViewHolder(binding.root) {
        fun bind(devJob: DevJob) {
            binding.devJob = devJob
            binding.executePendingBindings()
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<DevJob>() {
        override fun areItemsTheSame(oldItem: DevJob, newItem: DevJob): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: DevJob, newItem: DevJob): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DevJobViewHolder {
        return DevJobViewHolder(DevjobItemViewBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: DevJobViewHolder, position: Int) {
        val devJob = getItem(position)
        holder.bind(devJob)
        holder.itemView.setOnClickListener {
            onClickListener.onClick(devJob)
        }
    }

    class OnClickListener(val clickListener: (devJob: DevJob) -> Unit) {
        fun onClick(devJob: DevJob) = clickListener(devJob)
    }
}