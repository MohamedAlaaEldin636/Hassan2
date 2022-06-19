package com.maproductions.mohamedalaa.shared.presentation.adapters.viewHolders

import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.maproductions.mohamedalaa.shared.R
import com.maproductions.mohamedalaa.shared.core.customTypes.FileType
import com.maproductions.mohamedalaa.shared.core.customTypes.ImageOrVideo
import com.maproductions.mohamedalaa.shared.core.extensions.findNavControllerOfProject
import com.maproductions.mohamedalaa.shared.core.extensions.inflateLayout
import com.maproductions.mohamedalaa.shared.core.extensions.navigateDeepLinkWithoutOptions
import com.maproductions.mohamedalaa.shared.databinding.ItemImageOrVideoBinding
import com.maproductions.mohamedalaa.shared.databinding.ItemWalletBinding
import com.maproductions.mohamedalaa.shared.domain.wallet.ItemWallet

class VHItemWallet(parent: ViewGroup) : RecyclerView.ViewHolder(
    parent.context.inflateLayout(R.layout.item_wallet, parent)
) {

    private val binding = ItemWalletBinding.bind(itemView)

    fun bind(item: ItemWallet) {
        binding.dateTextView.text = item.date.orEmpty()

        val res = if (item.isMinus) {
            R.string.wallet_subtraction_var
        }else {
            R.string.wallet_addition_var
        }

        val reason = binding.root.context.run {
            "${getString(R.string.because_of)} ${item.getReadableReason(this)}"
        }

        val date = binding.root.context.run {
            if (item.date.isNullOrEmpty()) "" else "${getString(R.string.with_date)} ${item.date.orEmpty()}"
        }

        binding.descriptionTextView.text = binding.root.context.getString(res, item.amount.toString()).plus(
            " $reason $date"
        )
    }

}
