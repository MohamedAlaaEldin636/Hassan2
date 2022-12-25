package com.maproductions.mohamedalaa.hassanp.presentation.home.adapters.viewHolder

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.fragment.app.findFragment
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingDataAdapter
import androidx.paging.PagingSource
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.maproductions.mohamedalaa.hassanp.R
import com.maproductions.mohamedalaa.hassanp.databinding.ItemOrderInHomeBinding
import com.maproductions.mohamedalaa.shared.core.extensions.*
import com.maproductions.mohamedalaa.shared.core.extensions.bindingAdapter.setUrlViaGlideOrPlaceholder
import com.maproductions.mohamedalaa.shared.data.orders.repository.RepoOrder
import com.maproductions.mohamedalaa.shared.domain.home.DeliveryData
import com.maproductions.mohamedalaa.shared.R as SR
import com.maproductions.mohamedalaa.shared.domain.orders.ResponseOrder
import com.maproductions.mohamedalaa.shared.presentation.base.MABaseFragment
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

class VHItemOrderInHome(parent: ViewGroup, private val repoOrder: RepoOrder, private val refreshData: () -> Unit) : RecyclerView.ViewHolder(
    parent.context.inflateLayout(R.layout.item_order_in_home, parent)
) {

    private val binding = ItemOrderInHomeBinding.bind(itemView)

    private val context get() = binding.root.context

    init {
        binding.closeImageView.setOnClickListener { view ->
            val orderId = binding.materialCardView.tag as? Int ?: return@setOnClickListener

            val fragment = view.findFragment<MABaseFragment<*>>()

            fragment.executeOnGlobalLoadingAndAutoHandleRetryCancellable2(
                afterShowingLoading = {
                    repoOrder.rejectOrder(orderId)
                },
                afterHidingLoading = {
                    refreshData()

                    fragment.context?.showSuccessToast(it.message.orIfNullOrEmpty(
                        fragment.getString(SR.string.done_successfully)
                    ))
                }
            )
        }

        binding.materialButton.setOnClickListener { view ->
            val orderId = binding.materialCardView.tag as? Int ?: return@setOnClickListener

            val fragment = view.findFragment<MABaseFragment<*>>()

            fragment.executeOnGlobalLoadingAndAutoHandleRetryCancellable2(
                afterShowingLoading = {
                    repoOrder.acceptOrder(orderId)
                },
                afterHidingLoading = {
                    refreshData()

                    fragment.findNavControllerOfProject().navigateDeepLinkWithoutOptions(
                        "dialog-dest",
                        "com.grand.hassan.shared.accepted.order.dialog"
                    )
                }
            )
        }
    }

    @SuppressLint("SetTextI18n")
    fun bind(item: ResponseOrder) {
        binding.materialCardView.tag = item.id

        binding.imageImageView.setUrlViaGlideOrPlaceholder(item.user?.imageUrl)

        binding.nameTextView.text = item.user?.name

        binding.costTextView.text = item.total.roundHalfUpToIntOrFloat(1).toString() +
                " ${binding.root.context.getString(SR.string.currency_symbolic_text_caps)}"

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
