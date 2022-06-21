package com.maproductions.mohamedalaa.hassanu.presentation.main

import com.maproductions.mohamedalaa.hassanu.R
import com.maproductions.mohamedalaa.shared.R as SR
import com.maproductions.mohamedalaa.shared.presentation.main.SharedMainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : SharedMainActivity() {

    override val graphNavigationRes: Int
        get() = R.navigation.nav_main

    override val destinationsHideToolbar = setOf(
        R.id.dest_log_in,
        R.id.dest_user_bottom_nav,
    )

    override val destinationsIgnoreToolbarVisibility = setOf(
        R.id.dest_user_bottom_nav,
        R.id.dest_move_to_provider_app_dialog,
        R.id.dest_guest_please_login_dialog,
        R.id.done_adding_address_dialog,
        R.id.dest_del_address_check_dialog,
        R.id.dest_pending_provider_service_request_dialog,
        R.id.dest_rate_provider_2_dialog,
        R.id.dest_rate_provider_dialog,
        R.id.dest_get_discount_code_dialog,
        R.id.dest_accepted_provider_service_request_dialog,
    )

    override val topLevelDestinations = setOf(
        R.id.dest_user_bottom_nav,
    )

    override val notTransparentToolbarDestinations = setOf(
        -1
    )

    override val destinationsShowNotificationIcon = setOf(
        R.id.dest_user_bottom_nav,
    )

    override val pusherInterests = setOf(
        "users",
    )

    override val notCenteredTitleToolbarDestinations = setOf(
        R.id.dest_get_discounts,
    )

}