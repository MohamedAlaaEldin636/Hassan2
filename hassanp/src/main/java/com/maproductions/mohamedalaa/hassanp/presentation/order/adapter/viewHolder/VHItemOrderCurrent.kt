package com.maproductions.mohamedalaa.hassanp.presentation.order.adapter.viewHolder

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.google.gson.Gson
import com.maproductions.mohamedalaa.hassanp.R
import com.maproductions.mohamedalaa.shared.R as SR
import com.maproductions.mohamedalaa.hassanp.databinding.ItemOrderCurrentBinding
import com.maproductions.mohamedalaa.shared.core.extensions.*
import com.maproductions.mohamedalaa.shared.core.extensions.bindingAdapter.setUrlViaGlideOrIgnore
import com.maproductions.mohamedalaa.shared.core.extensions.bindingAdapter.setUrlViaGlideOrPlaceholder
import com.maproductions.mohamedalaa.shared.domain.orders.ResponseOrder
import timber.log.Timber
import kotlin.math.roundToInt

class VHItemOrderCurrent(parent: ViewGroup) : RecyclerView.ViewHolder(
    parent.context.inflateLayout(R.layout.item_order_current, parent)
) {

    private val binding = ItemOrderCurrentBinding.bind(itemView)

    private val context get() = binding.root.context

    init {
        binding.materialButton.setOnClickListener {
            val orderId = binding.materialCardView.tag as? Int ?: return@setOnClickListener

            it.findNavControllerOfProject().navigateDeepLinkWithOptions(
                "fragment-dest",
                "com.grand.hassan.shared.order.details",
                paths = arrayOf(orderId.toString())
            )
        }

        binding.providerCallMaterialCardView.setOnClickListener {
            val phone = binding.providerCallMaterialCardView.tag as? String ?: return@setOnClickListener

            it.context.launchDialNumber(phone)
        }

        binding.providerMSGMaterialCardView.setOnClickListener {
            Timber.e("providerMSGMaterialCardView reached 0")
            val orderId = binding.materialCardView.tag as? Int ?: return@setOnClickListener
            val providerId = binding.providerMSGMaterialCardView.tag as? Int ?: return@setOnClickListener
            val imageUrl = binding.providerNameTextView.tag as? String
            val otherName = binding.addressTextView.tag as? String ?: return@setOnClickListener
            Timber.e("providerMSGMaterialCardView reached 1 -> providerId : $providerId, orderId : $orderId, imageUrl : $imageUrl, otherName : $otherName")

            it.findNavControllerOfProject().navigateDeepLinkWithOptions(
                "fragment-dest",
                "com.grand.hassan.shared.chat.details",
                paths = arrayOf(providerId.toString(), orderId.toString())
            )
        }
    }

    @SuppressLint("SetTextI18n")
    fun bind(item: ResponseOrder) {
        binding.materialCardView.tag = item.id
        binding.providerCallMaterialCardView.tag = item.user.phone
        binding.providerMSGMaterialCardView.tag = item.user.id
        binding.providerNameTextView.tag = item.user.imageUrl
        binding.addressTextView.tag = item.user.name

        binding.imageMaterialCardView.setUrlViaGlideOrPlaceholder(item.user.imageUrl)
        binding.providerNameTextView.text = item.user.name

        // 3 هاشم الأشقر - القاهرة - بجوار برج
        binding.addressTextView.text = item.address?.address.orEmpty()
        // الأحد - أغسطس ٢٠٢٠/٥/٥ AM 15 : 08
        binding.dateAndTimeTextView.text = item.orderedAt

        binding.chipGroup.removeAllViewsInstanceOf<Chip>()
        for (service in item.services) {
            val chip = Chip(context).also {
                it.text = service.name.orEmpty()
            }

            chip.setChipBackgroundColorResource(SR.color.chip_themed_bg_color)

            binding.chipGroup.addView(chip)
        }
    }

}
