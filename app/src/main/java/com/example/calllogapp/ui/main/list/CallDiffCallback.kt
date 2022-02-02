package com.example.calllogapp.ui.main.list

import androidx.recyclerview.widget.DiffUtil
import com.example.calllogapp.model.CallRecord

class CallDiffCallback(
    private val oldList: List<CallRecord>,
    private val newList: List<CallRecord>
) : DiffUtil.Callback() {

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldList[oldItemPosition].beginning == newList[newItemPosition].beginning

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldList[oldItemPosition] == newList[newItemPosition]
}