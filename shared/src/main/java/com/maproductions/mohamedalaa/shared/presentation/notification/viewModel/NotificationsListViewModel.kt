package com.maproductions.mohamedalaa.shared.presentation.notification.viewModel

import androidx.lifecycle.ViewModel
import com.maproductions.mohamedalaa.shared.NavSharedArgs
import com.maproductions.mohamedalaa.shared.data.settings.repository.RepoSettings
import com.maproductions.mohamedalaa.shared.presentation.notification.adapters.RVItemNotification
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NotificationsListViewModel @Inject constructor(
    repoSettings: RepoSettings
) : ViewModel() {

    val notifications = repoSettings.getNotifications()

    val adapter = RVItemNotification()

}
