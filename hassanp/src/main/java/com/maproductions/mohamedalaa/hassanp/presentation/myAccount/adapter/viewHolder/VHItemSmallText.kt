package com.maproductions.mohamedalaa.hassanp.presentation.myAccount.adapter.viewHolder

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.maproductions.mohamedalaa.hassanp.presentation.myAccount.adapter.RVItemImageDel2
import com.maproductions.mohamedalaa.hassanp.R
import com.maproductions.mohamedalaa.hassanp.databinding.ItemSmallTextBinding
import com.maproductions.mohamedalaa.shared.R as SR
import com.maproductions.mohamedalaa.shared.core.customTypes.MAImage
import com.maproductions.mohamedalaa.shared.core.extensions.bindingAdapter.setImageUriViaGlideOrPlaceholderMAImage3
import com.maproductions.mohamedalaa.shared.core.extensions.findNavControllerOfProject
import com.maproductions.mohamedalaa.shared.core.extensions.inflateLayout
import com.maproductions.mohamedalaa.shared.core.extensions.navigateDeepLinkWithoutOptions
import com.maproductions.mohamedalaa.shared.databinding.ItemImageDel2Binding

class VHItemSmallText(parent: ViewGroup) : RecyclerView.ViewHolder(
    parent.context.inflateLayout(R.layout.item_small_text, parent)
) {

    private val binding = ItemSmallTextBinding.bind(itemView)

    fun bind(item: String) {
        binding.textView.text = item.orEmpty()
    }

}
