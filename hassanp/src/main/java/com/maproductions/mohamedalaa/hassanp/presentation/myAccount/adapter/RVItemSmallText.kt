package com.maproductions.mohamedalaa.hassanp.presentation.myAccount.adapter

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.maproductions.mohamedalaa.hassanp.presentation.myAccount.adapter.viewHolder.VHItemImageDel2
import com.maproductions.mohamedalaa.hassanp.presentation.myAccount.adapter.viewHolder.VHItemSmallText
import com.maproductions.mohamedalaa.shared.core.customTypes.MAImage

class RVItemSmallText : RecyclerView.Adapter<VHItemSmallText>() {

    private var list: List<String> = emptyList()

    override fun getItemCount(): Int = list.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHItemSmallText {
        return VHItemSmallText(parent)
    }

    override fun onBindViewHolder(holder: VHItemSmallText, position: Int) {
        holder.bind(list[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(list: List<String>) {
        this.list = list
        notifyDataSetChanged()
    }

}
