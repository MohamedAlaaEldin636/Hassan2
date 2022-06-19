package com.maproductions.mohamedalaa.shared.presentation.onBoard.adapters.viewHolder

import android.net.Uri
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.core.text.TextUtilsCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.Navigation
import androidx.paging.PagingData
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.gson.Gson
import com.maproductions.mohamedalaa.shared.R
import com.maproductions.mohamedalaa.shared.core.extensions.bindingAdapter.setUrlViaGlideOrIgnore
import com.maproductions.mohamedalaa.shared.core.extensions.inflateLayout
import com.maproductions.mohamedalaa.shared.databinding.ItemSliderOnBoardBinding
import com.maproductions.mohamedalaa.shared.domain.settings.ItemOnBoard
import com.smarteist.autoimageslider.SliderViewAdapter

class SliderVHOnBoard(parent: ViewGroup) : SliderViewAdapter.ViewHolder(
    parent.context.inflateLayout(R.layout.item_slider_on_board, parent)
) {

    private val binding = ItemSliderOnBoardBinding.bind(itemView)

    fun bind(itemOnBoard: ItemOnBoard) {
        binding.textView.text = HtmlCompat.fromHtml(
            itemOnBoard.textAsHtml,
            HtmlCompat.FROM_HTML_MODE_COMPACT
        )

        binding.imageView.setUrlViaGlideOrIgnore(itemOnBoard.imageUrl)
    }

}
