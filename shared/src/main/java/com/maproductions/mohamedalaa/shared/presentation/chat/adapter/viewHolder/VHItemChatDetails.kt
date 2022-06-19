package com.maproductions.mohamedalaa.shared.presentation.chat.adapter.viewHolder

import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.maproductions.mohamedalaa.shared.R
import com.maproductions.mohamedalaa.shared.core.extensions.bindingAdapter.setUrlViaGlideOrPlaceholder
import com.maproductions.mohamedalaa.shared.core.extensions.findNavControllerOfProject
import com.maproductions.mohamedalaa.shared.core.extensions.inflateLayout
import com.maproductions.mohamedalaa.shared.core.extensions.navigateDeepLinkWithoutOptions
import com.maproductions.mohamedalaa.shared.databinding.ItemChatOtherBinding
import com.maproductions.mohamedalaa.shared.databinding.ItemChatUserBinding
import com.maproductions.mohamedalaa.shared.presentation.conversations.ItemConversation

class VHItemChatDetails(
    parent: ViewGroup,
    isMyMsg: Boolean,
    private val getImage: () -> String
) : RecyclerView.ViewHolder(
    parent.context.inflateLayout(
        if (isMyMsg) R.layout.item_chat_user else R.layout.item_chat_other,
        parent
    )
) {

    private val userBinding = if (isMyMsg) ItemChatUserBinding.bind(itemView) else null
    private val otherBinding = if (!isMyMsg) ItemChatOtherBinding.bind(itemView) else null

    private val constraintLayout get() = userBinding?.constraintLayout ?: otherBinding!!.constraintLayout
    private val msgTextView get() = userBinding?.msgTextView ?: otherBinding!!.msgTextView
    private val msgImageView get() = userBinding?.msgImageView ?: otherBinding!!.msgImageView
    private val profileImageView get() = otherBinding?.imageImageView
    //private val videoIndicatorImageView get() = userBinding?.videoIndicatorImageView ?: otherBinding!!.videoIndicatorImageView
    private val timeTextView get() = userBinding?.timeTextView ?: otherBinding!!.timeTextView
    private val msgImageMaterialCardView get() = userBinding?.msgImageMaterialCardView ?: otherBinding!!.msgImageMaterialCardView
    private val dividerView get() = userBinding?.dividerView ?: otherBinding!!.dividerView
    private val imageMaterialCardView get() = otherBinding?.imageMaterialCardView

    init {
        imageMaterialCardView?.setOnClickListener {
            it.findNavControllerOfProject().navigateDeepLinkWithoutOptions(
                "dialog-dest",
                "com.grand.hassan.shared.showing.image.dialog",
                getImage()
            )
        }

        msgImageMaterialCardView.setOnClickListener {
            val url = constraintLayout.tag as? String ?: return@setOnClickListener

            it.findNavControllerOfProject().navigateDeepLinkWithoutOptions(
                "dialog-dest",
                "com.grand.hassan.shared.showing.image.dialog",
                url
            )
        }
    }

    fun bind(item: ItemConversation) {
        constraintLayout.tag = item.imageUrl

        profileImageView?.setUrlViaGlideOrPlaceholder(getImage())

        dividerView.isVisible = !item.message.isNullOrEmpty() && !item.imageUrl.isNullOrEmpty()

        msgTextView.text = item.message
        msgTextView.isVisible = !item.message.isNullOrEmpty()

        msgImageMaterialCardView.isVisible = !item.imageUrl.isNullOrEmpty()
        msgImageView.setUrlViaGlideOrPlaceholder(item.imageUrl)

        timeTextView.text = item.time
    }

}
