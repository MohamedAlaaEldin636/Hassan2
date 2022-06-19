package com.maproductions.mohamedalaa.hassanu.presentation.service.adapters.viewHolder

import android.graphics.Typeface
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.text.style.SubscriptSpan
import android.text.style.TypefaceSpan
import android.view.ViewGroup
import androidx.core.text.buildSpannedString
import androidx.core.text.set
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.maproductions.mohamedalaa.shared.R
import com.maproductions.mohamedalaa.shared.core.customTypes.MAImage
import com.maproductions.mohamedalaa.shared.core.extensions.bindingAdapter.setUriViaGlideOrPlaceholder
import com.maproductions.mohamedalaa.shared.core.extensions.bindingAdapter.setUrlViaGlideOrPlaceholder
import com.maproductions.mohamedalaa.shared.core.extensions.inflateLayout
import com.maproductions.mohamedalaa.shared.core.extensions.roundHalfUpToIntOrFloat
import com.maproductions.mohamedalaa.shared.databinding.ItemImageRectBinding
import com.maproductions.mohamedalaa.shared.databinding.ItemServiceNameAndPriceBinding
import com.maproductions.mohamedalaa.shared.domain.home.ServiceInCategoryWithCount
import kotlin.math.absoluteValue

class VHItemServiceNameAndPrice(parent: ViewGroup) : RecyclerView.ViewHolder(
    parent.context.inflateLayout(R.layout.item_service_name_and_price, parent)
) {

    private val binding = ItemServiceNameAndPriceBinding.bind(itemView)

    fun bind(item: ServiceInCategoryWithCount, bold: Boolean) {
        binding.priceTextView.text = buildSpannedString {
            val isPercent = item.serviceInCategory.price < 0

            val totalPrice = item.serviceInCategory.price.absoluteValue * item.count.toFloat()
            append(totalPrice.roundHalfUpToIntOrFloat(1).toString())

            if (isPercent) {
                append(" %")
            }else {
                val start = length
                append(binding.root.context.getString(R.string.currency_symbolic_text))
                val end = length
                this[start..end] = SubscriptSpan()
            }

            if (bold) {
                this[0..length] = StyleSpan(Typeface.BOLD)
            }
        }

        binding.nameTextView.text = buildSpannedString {
            append(item.serviceInCategory.name.orEmpty())

            if (item.count > 1) {
                append(" ")
                val start = length
                append("X ${item.count}")
                val end = length

                this[start..end] = ForegroundColorSpan(binding.root.context.getColor(R.color.colorPrimaryDark))
            }

            if (bold) {
                this[0..length] = StyleSpan(Typeface.BOLD)
            }
        }
    }

}
