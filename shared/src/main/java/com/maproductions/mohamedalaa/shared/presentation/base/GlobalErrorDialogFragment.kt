package com.maproductions.mohamedalaa.shared.presentation.base

import com.maproductions.mohamedalaa.shared.core.customTypes.GlobalError
import android.view.View
import android.view.Window
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.maproductions.mohamedalaa.shared.R
import com.maproductions.mohamedalaa.shared.presentation.main.viewModels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GlobalErrorDialogFragment : BaseOkCancelDialogFragment() {

    override val canceledOnTouchOutside = false

    private val args by navArgs<GlobalErrorDialogFragmentArgs>()

    override val okButtonText: String by lazy {
        requireContext().getString(R.string.retry)
    }

    override val cancelButtonText: String by lazy {
        if (args.cancellable) requireContext().getString(R.string.cancel) else requireContext().getString(R.string.back)
    }

    override val message: String by lazy {
        args.message
    }

    override fun onCreateDialogWindowChanges(window: Window) {
        super.onCreateDialogWindowChanges(window)

        window.attributes?.windowAnimations = R.style.ScaleDialogAnim
    }

    override fun onOkClick(view: View) {
        activityViewModel?.globalError?.value = GlobalError.Retry

        findNavController().navigateUp()
    }

    override fun onCancelClick(view: View) {
        activityViewModel?.globalError?.value = GlobalError.Cancel

        findNavController().navigateUp()
        if (!args.cancellable) {
            findNavController().navigateUp()
        }
    }

    override fun onDismissListener() {
        activityViewModel?.globalError?.value = GlobalError.Cancel
    }

}
