package com.maproductions.mohamedalaa.hassanu.presentation.order.adapters.viewHolders

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.maproductions.mohamedalaa.hassanu.R
import com.maproductions.mohamedalaa.hassanu.databinding.ItemOrderCurrentBinding
import com.maproductions.mohamedalaa.hassanu.databinding.ItemOrderFinishedBinding
import com.maproductions.mohamedalaa.shared.core.extensions.*
import com.maproductions.mohamedalaa.shared.core.extensions.bindingAdapter.setProgressBA
import com.maproductions.mohamedalaa.shared.core.extensions.bindingAdapter.setUrlViaGlideOrIgnore
import com.maproductions.mohamedalaa.shared.core.extensions.bindingAdapter.setUrlViaGlideOrPlaceholder
import com.maproductions.mohamedalaa.shared.domain.orders.ProviderInOrder
import com.maproductions.mohamedalaa.shared.domain.orders.ResponseOrder
import timber.log.Timber
import kotlin.math.roundToInt

class VHItemOrderFinished(parent: ViewGroup, private val gson: Gson) : RecyclerView.ViewHolder(
    parent.context.inflateLayout(R.layout.item_order_finished, parent)
) {

    private val binding = ItemOrderFinishedBinding.bind(itemView)

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

        binding.providerClickableView.setOnClickListener {
            val jsonOfProviderInOrder = binding.providerClickableView.tag as? String ?: return@setOnClickListener

            it.findNavControllerOfProject().navigateDeepLinkWithOptions(
                "fragment-dest",
                "com.grand.hassan.shared.provider.details",
                paths = arrayOf(jsonOfProviderInOrder)
            )
        }
    }

    @SuppressLint("SetTextI18n")
    fun bind(item: ResponseOrder) {
        binding.materialCardView.tag = item.id
        binding.providerClickableView.tag = item.provider?.toJson(gson)

        binding.imageImageView.setUrlViaGlideOrPlaceholder(item.provider?.imageUrl)
        binding.providerNameTextView2.text = item.provider?.name.orEmpty()
        binding.providerRatingBar2.setProgressBA(
            (item.provider?.averageRate.orZero() * 20f).roundToInt()
        )
        binding.providerRateTextView2.text = "(${item.provider?.averageRate.orZero().roundHalfUpToIntOrFloat(1)})"

        // نوع الخدمة : كهرباء
        binding.serviceTypeTextView.text = "${context.getString(com.maproductions.mohamedalaa.shared.R.string.service_type)} : ${item.category}"
        binding.orderNumberTextView.text = "${context.getString(com.maproductions.mohamedalaa.shared.R.string.order_number)} : ${item.orderNumber}"
        // 3 هاشم الأشقر - القاهرة - بجوار برج
        binding.addressTextView.text = item.address?.address.orEmpty()
        // الأحد - أغسطس ٢٠٢٠/٥/٥ AM 15 : 08
        binding.dateAndTimeTextView.text = item.orderedAt
    }

}
