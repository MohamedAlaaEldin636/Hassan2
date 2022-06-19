package com.maproductions.mohamedalaa.shared.presentation.base.adapters.viewHolders

import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.maproductions.mohamedalaa.shared.R
import com.maproductions.mohamedalaa.shared.core.customTypes.MAImage
import com.maproductions.mohamedalaa.shared.core.extensions.bindingAdapter.setUriViaGlideOrPlaceholder
import com.maproductions.mohamedalaa.shared.core.extensions.bindingAdapter.setUrlViaGlideOrIgnore
import com.maproductions.mohamedalaa.shared.core.extensions.bindingAdapter.setUrlViaGlideOrPlaceholder
import com.maproductions.mohamedalaa.shared.core.extensions.inflateLayout
import com.maproductions.mohamedalaa.shared.databinding.ItemImageRectBinding
import com.maproductions.mohamedalaa.shared.domain.settings.ResponseAddress

class VHItemImageRect(parent: ViewGroup, private val canDeleteItems: Boolean, delete: (String) -> Unit) : RecyclerView.ViewHolder(
    parent.context.inflateLayout(R.layout.item_image_rect, parent)
) {

    private val binding = ItemImageRectBinding.bind(itemView)

    init {
        binding.deleteImageView.setOnClickListener {
            val id = binding.materialCardView.tag as? String ?: return@setOnClickListener

            delete(id)
        }
    }

    fun bind(item: MAImage) {
        binding.deleteImageView.isVisible = canDeleteItems

        binding.materialCardView.tag = item.getId()

        when (item) {
            is MAImage.ILink -> binding.imageView.setUrlViaGlideOrPlaceholder(item.url)
            is MAImage.IUri -> binding.imageView.setUriViaGlideOrPlaceholder(item.uri)
        }
    }

}
