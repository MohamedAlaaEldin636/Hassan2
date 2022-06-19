package com.maproductions.mohamedalaa.shared.presentation.chat

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.maproductions.mohamedalaa.shared.R
import com.maproductions.mohamedalaa.shared.core.customTypes.PusherUtils
import com.maproductions.mohamedalaa.shared.core.extensions.*
import com.maproductions.mohamedalaa.shared.databinding.FragmentChatDetailsBinding
import com.maproductions.mohamedalaa.shared.databinding.FragmentConversationsBinding
import com.maproductions.mohamedalaa.shared.presentation.base.MABaseFragment
import com.maproductions.mohamedalaa.shared.presentation.chat.viewModel.ChatDetailsViewModel
import com.maproductions.mohamedalaa.shared.presentation.chat.viewModel.ConversationsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import timber.log.Timber

@AndroidEntryPoint
class ChatDetailsFragment : MABaseFragment<FragmentChatDetailsBinding>() {

    private val viewModel by viewModels<ChatDetailsViewModel>()

    private var currentlyRefreshing = false

    private val channelEvent by lazy {
        PusherUtils.getChannelEvent(
            PusherUtils.getChannelNameChatMessagesForOrder(viewModel.args.orderId),
            PusherUtils.EVENT_NAME_CHAT_MESSAGES_FOR_ORDER
        ) { data ->
            Timber.e("chat details messages ->\n$data")

            Handler(Looper.getMainLooper()).post {
                viewModel.adapter.refresh()
            }
        }
    }

    private val permissionImageRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions[Manifest.permission.READ_EXTERNAL_STORAGE] == true
                    && permissions[Manifest.permission.WRITE_EXTERNAL_STORAGE] == true
                    && permissions[Manifest.permission.CAMERA] == true -> {
                pickImageViaChooser()
            }
            permissions[Manifest.permission.READ_EXTERNAL_STORAGE] == true
                    && permissions[Manifest.permission.WRITE_EXTERNAL_STORAGE] == true -> {
                pickImage(false)
            }
            permissions[Manifest.permission.CAMERA] == true -> {
                pickImage(true)
            }
            else -> {
                requireContext().showNormalToast(getString(R.string.you_didn_t_accept_permission))
            }
        }
    }

    private val activityResultImageCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK) {
            val bitmap = it.data?.extras?.get("data") as? Bitmap ?: return@registerForActivityResult

            val uri = bitmap.getUriFromBitmapRetrievedByCamera(requireContext())

            viewModel.uri.value = uri
        }
    }

    private val activityResultImageGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK) {
            val uri = it.data?.data ?: return@registerForActivityResult

            viewModel.uri.value = uri
        }
    }

    override fun getLayoutId(): Int = R.layout.fragment_chat_details

    override fun initializeBindingVariables() {
        binding?.viewModel = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        channelEvent.subscribe()

        binding?.recyclerView?.layoutManager = LinearLayoutManager(
            requireContext(), LinearLayoutManager.VERTICAL, false
        ).also {
            it.stackFromEnd = true
        }
        binding?.recyclerView?.adapter = viewModel.adapter
            .withDefaultHeaderAndFooterAdapters(R.string.empty_string)

        viewModel.adapter.addLoadStateListener { loadStates ->
            if (loadStates.refresh == LoadState.Loading || loadStates.append == LoadState.Loading) {
                currentlyRefreshing = true
            }else if (currentlyRefreshing) {
                currentlyRefreshing = false

                binding?.recyclerView?.post {
                    binding?.recyclerView?.smoothScrollToPosition(
                        binding?.recyclerView?.adapter?.itemCount?.dec() ?: return@post
                    )
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.messages.collectLatest {
                    viewModel.adapter.submitData(it)
                }
            }
        }

        handleRetryAbleFlowWithMustHaveResultWithNullability(viewModel.retryAbleFlow) {
            val details = it.data ?: return@handleRetryAbleFlowWithMustHaveResultWithNullability

            viewModel.canSendMessage.value = details.isStatusFinished.not()

            viewModel.imageUrl.value = details.image

            viewModel.name.value = details.name

            viewModel.adapter.updateUrlOfProfileImage(details.image.orEmpty())
        }
    }

    fun pickImageOrRequestPermissions() {
        when {
            requireContext().checkSelfPermissionGranted(Manifest.permission.READ_EXTERNAL_STORAGE)
                    && requireContext().checkSelfPermissionGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    && requireContext().checkSelfPermissionGranted(Manifest.permission.CAMERA) -> {
                pickImageViaChooser()
            }
            /*requireContext().checkSelfPermissionGranted(Manifest.permission.READ_EXTERNAL_STORAGE)
                    && requireContext().checkSelfPermissionGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE) -> {
                pickImage(false)
            }
            requireContext().checkSelfPermissionGranted(Manifest.permission.CAMERA) -> {
                pickImage(true)
            }*/
            else -> {
                permissionImageRequest.launch(arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA,
                ))
            }
        }
    }

    private fun pickImageViaChooser() {
        val camera = getString(com.maproductions.mohamedalaa.shared.R.string.camera)
        val gallery = getString(com.maproductions.mohamedalaa.shared.R.string.gallery)
        val delete = if (viewModel.uri.value == null) null else getString(com.maproductions.mohamedalaa.shared.R.string.delete)

        binding?.cameraMaterialCardView?.showPopup(listOfNotNull(delete, camera, gallery)) {
            if (it.title?.toString() == delete) {
                viewModel.uri.value = null
            }else {
                pickImage(it.title?.toString() == camera)
            }
        }
    }

    private fun pickImage(fromCamera: Boolean) {
        if (fromCamera) {
            activityResultImageCamera.launch(Intent(MediaStore.ACTION_IMAGE_CAPTURE))
        }else {
            // From gallery
            activityResultImageGallery.launch(Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI))
        }
    }

    override fun onDestroyView() {
        channelEvent.unsubscribe()

        super.onDestroyView()
    }

}
