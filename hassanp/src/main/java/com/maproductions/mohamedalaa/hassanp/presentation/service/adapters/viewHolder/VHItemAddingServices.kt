package com.maproductions.mohamedalaa.hassanp.presentation.service.adapters.viewHolder

import android.text.style.SubscriptSpan
import android.view.ViewGroup
import androidx.core.text.buildSpannedString
import androidx.core.text.set
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.maproductions.mohamedalaa.hassanp.R
import com.maproductions.mohamedalaa.hassanp.databinding.ItemAddingServicesBinding
import com.maproductions.mohamedalaa.shared.R as SR
import com.maproductions.mohamedalaa.shared.core.extensions.getTagViaGson
import com.maproductions.mohamedalaa.shared.core.extensions.inflateLayout
import com.maproductions.mohamedalaa.shared.core.extensions.roundHalfUpToIntOrFloat
import com.maproductions.mohamedalaa.shared.core.extensions.setTagViaGson
import com.maproductions.mohamedalaa.shared.domain.home.ServiceInCategory

class VHItemAddingServices(
    parent: ViewGroup,
    private val gson: Gson,
    private val incrementOrDecrementThenGetNewCount: (position: Int, service: ServiceInCategory, increment: Boolean) -> Unit
) : RecyclerView.ViewHolder(
    parent.context.inflateLayout(R.layout.item_adding_services, parent)
) {

    private val binding = ItemAddingServicesBinding.bind(itemView)

    init {
        val action = fun (increment: Boolean): String? {
            val position = binding.linearLayout.tag as? Int ?: return null

            return binding.nameTextView.getTagViaGson<ServiceInCategory>(gson)?.run {
                incrementOrDecrementThenGetNewCount(position, this, increment).toString()
            }
        }

        binding.plusImageView.setOnClickListener {
            action(true)
        }

        binding.minusImageView.setOnClickListener {
            action(false)
        }
    }

    fun bind(position: Int, item: ServiceInCategory, count: Int) {
        binding.nameTextView.setTagViaGson(item, gson)
        binding.linearLayout.tag = position

        binding.nameTextView.text = item.name.orEmpty()

        binding.priceTextView.text = buildSpannedString {
            append(item.price.roundHalfUpToIntOrFloat(1).toString())

            val start = length
            append(binding.linearLayout.context.getString(SR.string.currency_symbolic_text))
            val end = length

            this[start..end] = SubscriptSpan()
        }

        binding.numberTextView.text = count.toString()
    }

}
