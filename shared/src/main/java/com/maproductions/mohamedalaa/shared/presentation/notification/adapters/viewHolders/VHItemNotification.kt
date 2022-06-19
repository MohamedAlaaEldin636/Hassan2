package com.maproductions.mohamedalaa.shared.presentation.notification.adapters.viewHolders

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.maproductions.mohamedalaa.shared.R
import com.maproductions.mohamedalaa.shared.core.extensions.findNavControllerOfProject
import com.maproductions.mohamedalaa.shared.core.extensions.inflateLayout
import com.maproductions.mohamedalaa.shared.core.extensions.navigateDeepLinkWithOptions
import com.maproductions.mohamedalaa.shared.databinding.ItemNotificationBinding
import com.maproductions.mohamedalaa.shared.domain.notifications.ItemNotification
import com.maproductions.mohamedalaa.shared.domain.notifications.NotificationType

class VHItemNotification(parent: ViewGroup) : RecyclerView.ViewHolder(
    parent.context.inflateLayout(R.layout.item_notification, parent)
) {

    private val binding = ItemNotificationBinding.bind(itemView)

    init {
        binding.materialCardView.setOnClickListener {
            val id = binding.materialCardView.tag as? Int ?: return@setOnClickListener

            it.findNavControllerOfProject().navigateDeepLinkWithOptions(
                "fragment-dest",
                "com.grand.hassan.order.additions",
                paths = arrayOf(id.toString())
            )
        }
    }

    fun bind(item: ItemNotification) {
        binding.materialCardView.tag = item.targetId
        binding.materialCardView.isClickable = item.notificationType == NotificationType.CONFIRM_ADDITIONAL_SERVICES

        binding.dateAndTimeTextView.text = item.createdAt
        binding.descriptionTextView.text = item.title
    }

}
