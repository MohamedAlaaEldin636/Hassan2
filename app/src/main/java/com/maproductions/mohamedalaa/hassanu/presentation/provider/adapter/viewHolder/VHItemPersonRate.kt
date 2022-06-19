package com.maproductions.mohamedalaa.hassanu.presentation.provider.adapter.viewHolder

import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.maproductions.mohamedalaa.shared.R
import com.maproductions.mohamedalaa.shared.core.customTypes.FileType
import com.maproductions.mohamedalaa.shared.core.customTypes.ImageOrVideo
import com.maproductions.mohamedalaa.shared.core.extensions.bindingAdapter.setProgressBA
import com.maproductions.mohamedalaa.shared.core.extensions.bindingAdapter.setUrlViaGlideOrPlaceholder
import com.maproductions.mohamedalaa.shared.core.extensions.findNavControllerOfProject
import com.maproductions.mohamedalaa.shared.core.extensions.inflateLayout
import com.maproductions.mohamedalaa.shared.core.extensions.navigateDeepLinkWithoutOptions
import com.maproductions.mohamedalaa.shared.core.extensions.orZero
import com.maproductions.mohamedalaa.shared.databinding.ItemImageOrVideoBinding
import com.maproductions.mohamedalaa.shared.databinding.ItemPersonRateBinding
import com.maproductions.mohamedalaa.shared.domain.orders.ReviewInProvider
import kotlin.math.roundToInt

class VHItemPersonRate(parent: ViewGroup) : RecyclerView.ViewHolder(
    parent.context.inflateLayout(R.layout.item_person_rate, parent)
) {

    private val binding = ItemPersonRateBinding.bind(itemView)

    fun bind(item: ReviewInProvider) {
        // name review rate img
        binding.nameTextView.text = item.userName.orEmpty()

        binding.descriptionTextView.text = item.review.orEmpty()

        binding.ratingBar.setProgressBA(
            (item.rate.orZero() * 20f).roundToInt()
        )

        binding.imageImageView.setUrlViaGlideOrPlaceholder(item.userImageUrl)
    }

}
