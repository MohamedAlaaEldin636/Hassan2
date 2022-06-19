package com.maproductions.mohamedalaa.hassanu.presentation.home.adapters.viewHolder

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.maproductions.mohamedalaa.hassanu.R
import com.maproductions.mohamedalaa.hassanu.databinding.ItemServiceInHomeBinding
import com.maproductions.mohamedalaa.shared.core.extensions.*
import com.maproductions.mohamedalaa.shared.core.extensions.bindingAdapter.setUrlViaGlideOrIgnore
import com.maproductions.mohamedalaa.shared.domain.home.DeliveryData
import com.maproductions.mohamedalaa.shared.domain.home.SliderHomeCategory

class VHServiceInHome(parent: ViewGroup, private val isGuest: Boolean, private val gson: Gson) : RecyclerView.ViewHolder(
    parent.context.inflateLayout(R.layout.item_service_in_home, parent)
) {

    private val binding = ItemServiceInHomeBinding.bind(itemView)

    init {
        val clickListener = View.OnClickListener {
            val id = binding.constraintLayout.tag as? Int ?: return@OnClickListener
            val name = binding.imageMaterialCardView.tag as? String ?: return@OnClickListener
            val jsonDeliveryData = binding.textView.tag as? String ?: return@OnClickListener

            if (isGuest) {
                it.findNavControllerOfProject().navigateDeepLinkWithoutOptions(
                    "fragment-dest",
                    "com.grand.hassan.shared.guest.please.login.dialog"
                )
            }else {
                it.findNavControllerOfProject().navigateDeepLinkWithOptions(
                    "fragment-dest",
                    "com.grand.hassan.shared.services.selection",
                    paths = arrayOf(name, id.toString(), jsonDeliveryData)
                )
            }
        }

        binding.constraintLayout.setOnClickListener(clickListener)
        binding.imageMaterialCardView.setOnClickListener(clickListener)
    }

    fun bind(slider: SliderHomeCategory) {
        binding.constraintLayout.tag = slider.id
        binding.imageMaterialCardView.tag = slider.name
        binding.textView.tag = DeliveryData(
            slider.deliveryFees, slider.orderMinPrice, slider.orderMinPriceForExtra
        ).toJson(gson)

        binding.textView.text = slider.name

        binding.imageView.setUrlViaGlideOrIgnore(slider.imageUrl)
    }

}
