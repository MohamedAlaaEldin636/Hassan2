package com.maproductions.mohamedalaa.hassanu.presentation.discount.viewModel

import android.view.View
import androidx.lifecycle.ViewModel
import com.maproductions.mohamedalaa.hassanu.presentation.discount.adapter.RVItemCodeOfDiscount
import com.maproductions.mohamedalaa.shared.core.customTypes.RetryAbleFlow
import com.maproductions.mohamedalaa.shared.core.extensions.findNavControllerOfProject
import com.maproductions.mohamedalaa.shared.core.extensions.navigateDeepLinkWithOptions
import com.maproductions.mohamedalaa.shared.data.codes.repository.RepoCodes
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CodesOfDiscountsViewModel @Inject constructor(
    repoCodes: RepoCodes,
) : ViewModel() {

    val codes = repoCodes.getPromoCodes()

    val adapter = RVItemCodeOfDiscount()

    fun getNewDiscountCode(view: View) {
        view.findNavControllerOfProject().navigateDeepLinkWithOptions(
            "fragment-dest",
            "com.grand.hassan.user.get.discounts"
        )
    }

}
