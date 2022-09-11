package com.maproductions.mohamedalaa.hassanp.core

import com.maproductions.mohamedalaa.hassanp.presentation.main.MainActivity
import com.maproductions.mohamedalaa.shared.presentation.base.MABaseFragment
import com.maproductions.mohamedalaa.shared.presentation.base.MADialogFragment
import com.maproductions.mohamedalaa.shared.presentation.main.viewModels.MainViewModel

fun MABaseFragment<*>.changeOrderNotOnTheWay(orderId: Int) {
    activityViewModel?.changeTrackingForOrder(
        orderId, false, activity as? MainViewModel.Listener ?: return
    )
}
