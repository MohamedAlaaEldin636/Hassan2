package com.maproductions.mohamedalaa.hassanu.presentation.order.adapters.viewHolders

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.maproductions.mohamedalaa.hassanu.databinding.ItemOrderPendingBinding
import com.maproductions.mohamedalaa.shared.R as SR
import com.maproductions.mohamedalaa.hassanu.R
import com.maproductions.mohamedalaa.shared.core.extensions.*
import com.maproductions.mohamedalaa.shared.databinding.ItemNotificationBinding
import com.maproductions.mohamedalaa.shared.domain.notifications.ItemNotification
import com.maproductions.mohamedalaa.shared.domain.notifications.NotificationType
import com.maproductions.mohamedalaa.shared.domain.orders.ResponseOrder

class VHItemOrderPending(parent: ViewGroup) : RecyclerView.ViewHolder(
    parent.context.inflateLayout(R.layout.item_order_pending, parent)
) {

    private val binding = ItemOrderPendingBinding.bind(itemView)

    private val context get() = binding.root.context

    init {
        binding.showDetailsMaterialButton.setOnClickListener {
            val orderId = binding.materialCardView.tag as? Int ?: return@setOnClickListener

            it.findNavControllerOfProject().navigateDeepLinkWithOptions(
                "fragment-dest",
                "com.grand.hassan.shared.order.details",
                paths = arrayOf(orderId.toString())
            )
        }

        binding.cancelOrderMaterialButton.setOnClickListener {
            val orderId = binding.materialCardView.tag as? Int ?: return@setOnClickListener

            val cancellationFeesPercent = binding.cancelOrderMaterialButton.tag as? Float
                ?: return@setOnClickListener

            val text = if (cancellationFeesPercent > 0f) {
                context.getString(SR.string.are_you_sure_about_order_cancellation_with_fees_part_1) +
                        "$cancellationFeesPercent" +
                        context.getString(SR.string.are_you_sure_about_order_cancellation_with_fees_part_2)
            }else {
                context.getString(SR.string.are_you_sure_about_order_cancellation)
            }

            it.findNavControllerOfProject().navigateDeepLinkWithoutOptions(
                "dialog-dest",
                "com.grand.hassan.shared.cancel.order.dialog",
                orderId.toString(),
                text
            )
        }
    }

    @SuppressLint("SetTextI18n")
    fun bind(item: ResponseOrder) {
        binding.materialCardView.tag = item.id
        binding.cancelOrderMaterialButton.tag = item.cancellationFeesPercent.roundHalfUpToIntOrFloat(1).toFloat()

        // نوع الخدمة : كهرباء
        binding.serviceTypeTextView.text = "${context.getString(SR.string.service_type)} : ${item.category}"
        binding.orderNumberTextView.text = "${context.getString(SR.string.order_number)} : ${item.orderNumber}"
        // 3 هاشم الأشقر - القاهرة - بجوار برج
        binding.addressTextView.text = item.address?.address.orEmpty()
        // الأحد - أغسطس ٢٠٢٠/٥/٥ AM 15 : 08
        binding.dateAndTimeTextView.text = item.orderedAt
    }

}
