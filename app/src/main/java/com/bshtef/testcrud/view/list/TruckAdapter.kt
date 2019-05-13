package com.bshtef.testcrud.view.list

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bshtef.testcrud.R
import com.bshtef.testcrud.view.base.TruckSimpleView
import kotlinx.android.synthetic.main.item_truck.view.*

class TruckAdapter(private val listener: TruckClickListener) :
    ListAdapter<TruckSimpleView, TruckAdapter.ItemViewHolder>(
        DiffCallback()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_truck, parent, false),
            listener
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ItemViewHolder(itemView: View, private val listener: TruckClickListener) : RecyclerView.ViewHolder(itemView) {

        private val name: TextView = itemView.findViewById(R.id.name)
        private val price: TextView = itemView.findViewById(R.id.price)
        private val comment: TextView = itemView.findViewById(R.id.comment)
        private val textId: TextView = itemView.findViewById(R.id.id)


        @SuppressLint("SetTextI18n")
        fun bind(item: TruckSimpleView) = with(itemView) {

            name.text = item.name
            price.text = item.price.toString()
            comment.text = item.comment
            textId.text = "#${item.id}"

            setOnClickListener {
                listener.onClick(item)
            }

            delete.setOnClickListener {
                listener.onRemoveClick(item)
            }
        }
    }


}

class DiffCallback : DiffUtil.ItemCallback<TruckSimpleView>() {
    override fun areItemsTheSame(oldItem: TruckSimpleView, newItem: TruckSimpleView): Boolean {
        return oldItem.id == newItem.id
    }
    override fun areContentsTheSame(oldItem: TruckSimpleView, newItem: TruckSimpleView): Boolean {
        return oldItem == newItem
    }
}

interface TruckClickListener {
    fun onClick(truck: TruckSimpleView)
    fun onRemoveClick(truck: TruckSimpleView)
}