package com.maproductions.mohamedalaa.hassanu.presentation.home.adapters.viewHolder

import android.net.Uri
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.findFragment
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.Navigation
import androidx.paging.PagingData
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.gson.Gson
import com.maproductions.mohamedalaa.hassanu.R
import com.maproductions.mohamedalaa.shared.core.customTypes.SliderLinkType
import com.maproductions.mohamedalaa.shared.core.extensions.*
import com.maproductions.mohamedalaa.shared.core.extensions.bindingAdapter.setUrlViaGlideOrIgnore
import com.maproductions.mohamedalaa.shared.R as SR
import com.maproductions.mohamedalaa.shared.databinding.ItemImageBinding
import com.maproductions.mohamedalaa.shared.databinding.ItemImageHomeUserSliderBinding
import com.maproductions.mohamedalaa.shared.domain.home.DeliveryData
import com.maproductions.mohamedalaa.shared.domain.home.SliderHomeUser
import com.smarteist.autoimageslider.SliderViewAdapter

class SliderVHHomeUser(parent: ViewGroup, private val gson: Gson, private val isGuest: Boolean) : SliderViewAdapter.ViewHolder(
    parent.context.inflateLayout(SR.layout.item_image_home_user_slider, parent)
) {

    private val binding = ItemImageHomeUserSliderBinding.bind(itemView)

    init {
        binding.imageView.setOnClickListener {
            val slider = binding.imageView.getTagViaGson<SliderHomeUser>(gson, SR.id.view_holder_id)
                ?: return@setOnClickListener

            if (slider.linkType == SliderLinkType.IMAGE) {
                it.findNavControllerOfProject().navigateDeepLinkWithoutOptions(
                    "dialog-dest",
                    "com.grand.hassan.shared.showing.image.dialog",
                    slider.imageUrl
                )
            }else {
                if (isGuest) {
                    it.findNavControllerOfProject().navigateDeepLinkWithoutOptions(
                        "fragment-dest",
                        "com.grand.hassan.shared.guest.please.login.dialog"
                    )
                }else {
                    it.findNavControllerOfProject().navigateDeepLinkWithOptions(
                        "fragment-dest",
                        "com.grand.hassan.shared.services.selection",
                        paths = arrayOf(
                            slider.category?.name.orEmpty(),
                            slider.linkId.toString(),
                            slider.category?.let { category ->
                                DeliveryData(
                                    category.deliveryFees,
                                    category.orderMinPrice,
                                    category.orderMinPriceForExtra
                                ).toJson(gson)
                            }.orEmpty()
                        )
                    )
                }
            }
        }
    }

    fun bind(slider: SliderHomeUser) {
        binding.imageView.setTagViaGson(slider, gson, SR.id.view_holder_id)

        binding.imageView.setUrlViaGlideOrIgnore(slider.imageUrl)
    }

}