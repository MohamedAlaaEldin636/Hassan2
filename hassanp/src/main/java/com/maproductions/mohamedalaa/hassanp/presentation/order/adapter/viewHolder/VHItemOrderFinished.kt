package com.maproductions.mohamedalaa.hassanp.presentation.order.adapter.viewHolder

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.maproductions.mohamedalaa.hassanp.R
import com.maproductions.mohamedalaa.hassanp.databinding.ItemOrderCurrentBinding
import com.maproductions.mohamedalaa.hassanp.databinding.ItemOrderFinishedBinding
import com.maproductions.mohamedalaa.shared.core.extensions.*
import com.maproductions.mohamedalaa.shared.core.extensions.bindingAdapter.setUrlViaGlideOrPlaceholder
import com.maproductions.mohamedalaa.shared.domain.orders.ResponseOrder

class VHItemOrderFinished(parent: ViewGroup) : RecyclerView.ViewHolder(
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
    }

    @SuppressLint("SetTextI18n")
    fun bind(item: ResponseOrder) {
        binding.materialCardView.tag = item.id

        binding.costTextView.text = item.total.roundHalfUpToIntOrFloat(1).toString() +
                " ${binding.root.context.getString(com.maproductions.mohamedalaa.shared.R.string.currency_symbolic_text_caps)}"

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

            chip.setChipBackgroundColorResource(com.maproductions.mohamedalaa.shared.R.color.chip_themed_bg_color)

            binding.chipGroup.addView(chip)
        }
    }

}
