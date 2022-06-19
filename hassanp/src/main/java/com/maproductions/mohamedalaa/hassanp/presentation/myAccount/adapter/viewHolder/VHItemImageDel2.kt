package com.maproductions.mohamedalaa.hassanp.presentation.myAccount.adapter.viewHolder

import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.gson.Gson
import com.maproductions.mohamedalaa.hassanp.presentation.myAccount.adapter.RVItemImageDel2
import com.maproductions.mohamedalaa.shared.R
import com.maproductions.mohamedalaa.shared.core.customTypes.FileType
import com.maproductions.mohamedalaa.shared.core.customTypes.ImageOrVideo
import com.maproductions.mohamedalaa.shared.core.customTypes.MAImage
import com.maproductions.mohamedalaa.shared.core.extensions.*
import com.maproductions.mohamedalaa.shared.core.extensions.bindingAdapter.setImageUriViaGlideOrPlaceholderMAImage
import com.maproductions.mohamedalaa.shared.core.extensions.bindingAdapter.setImageUriViaGlideOrPlaceholderMAImage3
import com.maproductions.mohamedalaa.shared.core.extensions.bindingAdapter.setUrlViaGlideOrPlaceholder
import com.maproductions.mohamedalaa.shared.databinding.ItemImageDel2Binding
import com.maproductions.mohamedalaa.shared.databinding.ItemImageOrVideoBinding
import com.maproductions.mohamedalaa.shared.presentation.myAccount.ItemUri

class VHItemImageDel2(
    parent: ViewGroup,
    private val listener: RVItemImageDel2.Listener,
    private val gson: Gson
) : RecyclerView.ViewHolder(
    parent.context.inflateLayout(R.layout.item_image_del_2, parent)
) {

    private val binding = ItemImageDel2Binding.bind(itemView)

    init {
        binding.shapeableImageView.setOnClickListener {
            val link = (binding.constraintLayout.tag as? String)?.let { json ->
                json.fromJsonOrNull<Pair<Int, String>>(gson)?.second
            } ?: return@setOnClickListener
            val isUri = binding.deleteImageView.tag as? Boolean ?: return@setOnClickListener

            it.findNavControllerOfProject().navigateDeepLinkWithoutOptions(
                "dialog-dest",
                "com.grand.hassan.shared.showing.image.dialog.isUri",
                link,
                isUri.toString()
            )
        }

        binding.deleteImageView.setOnClickListener { view ->
            val (id, maImageId) = (binding.constraintLayout.tag as? String)?.let { json ->
                json.fromJsonOrNull<Pair<Int, String>>(gson)
            } ?: return@setOnClickListener

            listener.delete(view, id, maImageId)
        }
    }

    fun bind(item: ItemUri) {
        binding.constraintLayout.tag = (item.id to item.maImage.getId()).toJson(gson)
        binding.deleteImageView.tag = item.maImage is MAImage.IUri

        binding.shapeableImageView.setImageUriViaGlideOrPlaceholderMAImage3(item.maImage)
    }

}
