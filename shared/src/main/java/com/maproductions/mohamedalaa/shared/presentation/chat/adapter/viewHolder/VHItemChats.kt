package com.maproductions.mohamedalaa.shared.presentation.chat.adapter.viewHolder

import android.text.style.SubscriptSpan
import android.view.ViewGroup
import androidx.core.text.buildSpannedString
import androidx.core.text.set
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.maproductions.mohamedalaa.shared.R
import com.maproductions.mohamedalaa.shared.core.extensions.*
import com.maproductions.mohamedalaa.shared.core.extensions.bindingAdapter.setUrlViaGlideOrPlaceholder
import com.maproductions.mohamedalaa.shared.databinding.ItemConversationBinding
import com.maproductions.mohamedalaa.shared.domain.home.ServiceInCategory
import com.maproductions.mohamedalaa.shared.presentation.conversations.ItemConversation
import timber.log.Timber

class VHItemChats(
    parent: ViewGroup,
) : RecyclerView.ViewHolder(
    parent.context.inflateLayout(R.layout.item_conversation, parent)
) {

    private val binding = ItemConversationBinding.bind(itemView)

    init {
        binding.materialCardView.setOnClickListener {
            val otherId = binding.materialCardView.tag as? Int ?: return@setOnClickListener
            val orderId = binding.constraintLayout.tag as? Int ?: return@setOnClickListener
            val imageUrl = binding.timeTextView.tag as? String
            val otherName = binding.nameTextView.tag as? String ?: return@setOnClickListener
            val isStatusFinished = binding.msgTextView.tag as? Boolean ?: return@setOnClickListener

            Timber.e("chat history otherId : $otherId, orderId : $orderId, imageUrl : $imageUrl, otherName : $otherName")

            it.findNavControllerOfProject().navigateDeepLinkWithOptions(
                "fragment-dest",
                "com.grand.hassan.shared.chat.details",
                paths = arrayOf(
                    otherId.toString(),
                    orderId.toString(),
                )
            )
        }
    }

    fun bind(item: ItemConversation) {
        binding.materialCardView.tag = item.user.id
        binding.constraintLayout.tag = item.orderId.orZero()
        binding.timeTextView.tag = item.user.imageUrl
        binding.nameTextView.tag = item.user.name
        binding.msgTextView.tag = item.isStatusFinished

        binding.nameTextView.text = item.user.name
        binding.msgTextView.text = item.message.orEmpty()
        binding.timeTextView.text = item.time
        binding.imageView.setUrlViaGlideOrPlaceholder(item.user.imageUrl)
    }

}
