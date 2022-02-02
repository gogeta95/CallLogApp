package com.example.calllogapp.ui.main.list

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.calllogapp.R
import com.example.calllogapp.model.CallRecord

class CallLogRecyclerAdapter(context: Context) :
    RecyclerView.Adapter<CallLogRecyclerAdapter.CallLogViewHolder>() {

    val items = mutableListOf<CallRecord>()
    private val layoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CallLogViewHolder {
        val view = layoutInflater.inflate(R.layout.call_item, parent, false)
        return CallLogViewHolder(view)
    }

    override fun onBindViewHolder(holder: CallLogViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    class CallLogViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val name: TextView = itemView.findViewById(R.id.name)
        private val duration: TextView = itemView.findViewById(R.id.duration)

        fun bind(item: CallRecord) {
            name.text = if (item.name.isNullOrBlank()) item.number else item.name
            duration.text = itemView.context.getString(R.string.s_seconds, item.duration)
        }
    }
}