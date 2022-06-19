package com.maproductions.mohamedalaa.shared.presentation.chat.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.maproductions.mohamedalaa.shared.NavSharedArgs
import com.maproductions.mohamedalaa.shared.data.conversations.repository.RepoConversations
import com.maproductions.mohamedalaa.shared.presentation.chat.adapter.RVItemChats
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ConversationsViewModel @Inject constructor(
    application: Application,
    repoConversations: RepoConversations,
    val navArgs: NavSharedArgs
) : AndroidViewModel(application) {

    val chats = repoConversations.getConversationsList()

    val adapter = RVItemChats()

}
