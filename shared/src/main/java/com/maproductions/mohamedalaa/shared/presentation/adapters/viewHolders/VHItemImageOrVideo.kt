package com.maproductions.mohamedalaa.shared.presentation.adapters.viewHolders

import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.maproductions.mohamedalaa.shared.R
import com.maproductions.mohamedalaa.shared.core.customTypes.FileType
import com.maproductions.mohamedalaa.shared.core.customTypes.ImageOrVideo
import com.maproductions.mohamedalaa.shared.core.extensions.bindingAdapter.setUrlViaGlideOrIgnore
import com.maproductions.mohamedalaa.shared.core.extensions.findNavControllerOfProject
import com.maproductions.mohamedalaa.shared.core.extensions.inflateLayout
import com.maproductions.mohamedalaa.shared.core.extensions.navigateDeepLinkWithoutOptions
import com.maproductions.mohamedalaa.shared.databinding.ItemImageOrVideoBinding
import com.maproductions.mohamedalaa.shared.domain.settings.ResponseAddress

class VHItemImageOrVideo(parent: ViewGroup) : RecyclerView.ViewHolder(
    parent.context.inflateLayout(R.layout.item_image_or_video, parent)
) {

    private val binding = ItemImageOrVideoBinding.bind(itemView)

    init {
        binding.imageView.setOnClickListener {
            val link = binding.materialCardView.tag as? String ?: return@setOnClickListener
            val isImage = binding.frameLayout.tag as? Boolean ?: return@setOnClickListener

            if (isImage) {
                it.findNavControllerOfProject().navigateDeepLinkWithoutOptions(
                    "dialog-dest",
                    "com.grand.hassan.shared.showing.image.dialog",
                    link
                )
            }else {
                it.findNavControllerOfProject().navigateDeepLinkWithoutOptions(
                    "dialog-dest",
                    "com.grand.hassan.shared.video.player.dialog",
                    link
                )
            }
        }
    }

    fun bind(item: ImageOrVideo) {
        val isImage = item.fileType == FileType.IMAGE

        binding.materialCardView.tag = item.fileUrl
        binding.frameLayout.tag = isImage

        val options = RequestOptions().let {
            if (isImage) it else it.frame(1)
        }
        Glide.with(binding.imageView.context)
            .load(item.fileUrl)
            .apply(options)
            .placeholder(R.drawable.ic_logo_app_placeholder)
            .error(R.drawable.ic_logo_app_placeholder)
            .into(binding.imageView)

        binding.videoIndicatorImageView.isVisible = isImage.not()
    }

}
