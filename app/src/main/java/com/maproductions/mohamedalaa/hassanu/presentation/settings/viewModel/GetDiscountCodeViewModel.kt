package com.maproductions.mohamedalaa.hassanu.presentation.settings.viewModel

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.maproductions.mohamedalaa.shared.R
import com.maproductions.mohamedalaa.hassanu.presentation.settings.GetDiscountCodeDialogFragmentArgs
import com.maproductions.mohamedalaa.shared.core.extensions.copyToClipboard
import com.maproductions.mohamedalaa.shared.core.extensions.findNavControllerOfProject
import com.maproductions.mohamedalaa.shared.core.extensions.myApp
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GetDiscountCodeViewModel @Inject constructor(
    application: Application,
    private val args: GetDiscountCodeDialogFragmentArgs
) : AndroidViewModel(application) {

    val text by lazy {
        myApp.getString(R.string.var_points_have_been_replaced_by_discount_code, args.points.toString())
    }

    val code = "(${args.code})"

    fun useCode(view: View) {
        view.context.copyToClipboard(args.code)

        view.findNavControllerOfProject().navigateUp()
    }

}
