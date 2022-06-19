package com.maproductions.mohamedalaa.hassanu.presentation.address.viewModel

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import com.maproductions.mohamedalaa.hassanu.R
import com.maproductions.mohamedalaa.shared.R as SR
import com.maproductions.mohamedalaa.hassanu.presentation.address.DoneAddingAddressDialogFragmentArgs
import com.maproductions.mohamedalaa.shared.core.extensions.findNavControllerOfProject
import com.maproductions.mohamedalaa.shared.core.extensions.myApp
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DoneAddingAddressViewModel @Inject constructor(
    application: Application,
    args: DoneAddingAddressDialogFragmentArgs
) : AndroidViewModel(application) {

    val text = if (args.isAdditionNotDeletion) {
        myApp.getString(SR.string.new_address_has_been_added_successfully)
    }else {
        myApp.getString(SR.string.deletion_of_address_is_confirmed)
    }

    fun done(view: View) {
        view.findNavControllerOfProject().navigateUp()
    }

}
