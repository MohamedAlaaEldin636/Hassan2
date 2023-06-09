package com.maproductions.mohamedalaa.shared.core.extensions

import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.maproductions.mohamedalaa.shared.core.customTypes.MAToolbarOnDestinationChangedListener

fun Toolbar.setupWithNavControllerMA(
    navController: NavController,
    configuration: AppBarConfiguration = AppBarConfiguration(navController.graph)
) {
    navController.addOnDestinationChangedListener(
        MAToolbarOnDestinationChangedListener(this, configuration)
    )
    setNavigationOnClickListener { NavigationUI.navigateUp(navController, configuration) }
}
