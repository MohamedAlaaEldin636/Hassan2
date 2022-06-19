package com.maproductions.mohamedalaa.hassanp.presentation.myAccount.adapter.viewHolder

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.maproductions.mohamedalaa.hassanp.R
import com.maproductions.mohamedalaa.shared.R as SR
import com.maproductions.mohamedalaa.hassanp.databinding.ItemReviewBinding
import com.maproductions.mohamedalaa.hassanp.databinding.ItemSmallTextBinding
import com.maproductions.mohamedalaa.shared.core.extensions.bindingAdapter.setProgressBA
import com.maproductions.mohamedalaa.shared.core.extensions.inflateLayout
import com.maproductions.mohamedalaa.shared.core.extensions.orZero
import com.maproductions.mohamedalaa.shared.core.extensions.roundHalfUpToIntOrFloat
import com.maproductions.mohamedalaa.shared.domain.settings.ItemReview
import kotlin.math.roundToInt

class VHItemReview(parent: ViewGroup) : RecyclerView.ViewHolder(
    parent.context.inflateLayout(R.layout.item_review, parent)
) {

    private val binding = ItemReviewBinding.bind(itemView)

    private val oneCharEllipsis by lazy {
        binding.root.context.getString(SR.string.one_char_ellipsis)
    }

    fun bind(item: ItemReview) {
        binding.dateTextView.text = item.createdAt.orEmpty()

        binding.ratingBar.setProgressBA(
            (item.rate.orZero() * 20f).roundToInt()
        )

        binding.nameTextView.text = item.userName.orEmpty().let {
            if (it.length <= 7) it else "${it.substring(0, 6)}$oneCharEllipsis"
        }

        binding.reviewTextView.text = item.review.orEmpty()
    }

}
