package com.maproductions.mohamedalaa.shared.presentation.chat.viewModel

import android.app.Application
import android.net.Uri
import android.view.View
import androidx.fragment.app.findFragment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.maproductions.mohamedalaa.shared.NavSharedArgs
import com.maproductions.mohamedalaa.shared.R
import com.maproductions.mohamedalaa.shared.core.customTypes.RetryAbleFlow
import com.maproductions.mohamedalaa.shared.core.customTypes.switchMapMultiple2
import com.maproductions.mohamedalaa.shared.core.extensions.*
import com.maproductions.mohamedalaa.shared.data.api.ApiConst
import com.maproductions.mohamedalaa.shared.data.conversations.repository.RepoConversations
import com.maproductions.mohamedalaa.shared.presentation.chat.ChatDetailsFragment
import com.maproductions.mohamedalaa.shared.presentation.chat.ChatDetailsFragmentArgs
import com.maproductions.mohamedalaa.shared.presentation.chat.adapter.RVItemChatDetails
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChatDetailsViewModel @Inject constructor(
    application: Application,
    val args: ChatDetailsFragmentArgs,
    private val repoConversations: RepoConversations,
    navArgs: NavSharedArgs,
) : AndroidViewModel(application) {

    val retryAbleFlow = RetryAbleFlow {
        repoConversations.getChatMessagesRelatedData(args.receiverId, args.orderId)
    }

    val canSendMessage = MutableLiveData(true)

    val messages = repoConversations.getChatMessages(args.receiverId, args.orderId)

    val adapter = RVItemChatDetails(navArgs.isUserNotProvider)

    val imageUrl = MutableLiveData("")

    val name = MutableLiveData("")

    val message = MutableLiveData("")

    val uri = MutableLiveData<Uri?>()

    val canSend = switchMapMultiple2(message, uri) {
        !message.value.isNullOrEmpty() || uri.value != null
    }

    fun goBack(view: View) {
        view.findNavControllerOfProject().navigateUp()
    }

    fun sendMsg(view: View) {
        if (canSend.value != true) {
            return view.context.showErrorToast(view.context.getString(R.string.at_least_name_or_image_required))
        }

        val fragment = view.findFragment<ChatDetailsFragment>()

        fragment.executeOnGlobalLoadingAndAutoHandleRetryCancellable(
            afterShowingLoading = {
                repoConversations.sendChatMessage(
                    args.receiverId,
                    args.orderId,
                    message.value,
                    uri.value?.createMultipartBodyPart(myApp, ApiConst.Query.IMAGE)
                )
            },
            afterHidingLoading = {
                uri.value = null
                message.value = ""
                adapter.refresh()
            }
        )
    }

    fun pickImage(view: View) {
        view.findFragment<ChatDetailsFragment>().pickImageOrRequestPermissions()
    }

}
