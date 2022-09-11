package com.maproductions.mohamedalaa.shared.core.extensions

import com.maproductions.mohamedalaa.shared.presentation.base.MABaseFragment
import com.maproductions.mohamedalaa.shared.presentation.base.MADialogFragment
import com.maproductions.mohamedalaa.shared.presentation.main.viewModels.MainViewModel

fun MADialogFragment<*>.changeOrderNotOnTheWay(orderId: Int) {
    activityViewModel?.changeTrackingForOrder(
        orderId, false, activity as? MainViewModel.Listener ?: return
    )
}
