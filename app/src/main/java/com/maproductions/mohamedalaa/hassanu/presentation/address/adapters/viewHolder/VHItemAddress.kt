package com.maproductions.mohamedalaa.hassanu.presentation.address.adapters.viewHolder

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.maproductions.mohamedalaa.hassanu.R
import com.maproductions.mohamedalaa.hassanu.databinding.ItemAddressBinding
import com.maproductions.mohamedalaa.hassanu.databinding.ItemServiceInHomeBinding
import com.maproductions.mohamedalaa.hassanu.presentation.address.adapters.RVItemAddress
import com.maproductions.mohamedalaa.shared.core.extensions.bindingAdapter.setUrlViaGlideOrIgnore
import com.maproductions.mohamedalaa.shared.core.extensions.findNavControllerOfProject
import com.maproductions.mohamedalaa.shared.core.extensions.inflateLayout
import com.maproductions.mohamedalaa.shared.core.extensions.navigateDeepLinkWithOptions
import com.maproductions.mohamedalaa.shared.core.extensions.navigateDeepLinkWithoutOptions
import com.maproductions.mohamedalaa.shared.domain.home.SliderHomeCategory
import com.maproductions.mohamedalaa.shared.domain.settings.ResponseAddress

class VHItemAddress(parent: ViewGroup, private val listener: RVItemAddress.Listener) : RecyclerView.ViewHolder(
    parent.context.inflateLayout(R.layout.item_address, parent)
) {

    private val binding = ItemAddressBinding.bind(itemView)

    init {
        binding.imageView.setOnClickListener {
            val id = binding.materialCardView.tag as? Int ?: return@setOnClickListener

            listener.deleteAddress(it, id)
        }
    }

    fun bind(item: ResponseAddress) {
        binding.materialCardView.tag = item.id

        binding.titleTextView.text = item.title

        binding.bodyTextView.text = item.address
    }

}
