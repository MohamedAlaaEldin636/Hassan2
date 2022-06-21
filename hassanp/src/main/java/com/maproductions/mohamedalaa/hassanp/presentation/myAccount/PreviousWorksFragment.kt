package com.maproductions.mohamedalaa.hassanp.presentation.myAccount

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.maproductions.mohamedalaa.hassanp.R
import com.maproductions.mohamedalaa.shared.R as SR
import com.maproductions.mohamedalaa.hassanp.databinding.FragmentMoreBinding
import com.maproductions.mohamedalaa.hassanp.databinding.FragmentPreviousWorksBinding
import com.maproductions.mohamedalaa.hassanp.presentation.more.viewModel.MoreViewModel
import com.maproductions.mohamedalaa.hassanp.presentation.myAccount.viewModel.PreviousWorksViewModel
import com.maproductions.mohamedalaa.shared.core.customTypes.FileType
import com.maproductions.mohamedalaa.shared.core.customTypes.MAImage
import com.maproductions.mohamedalaa.shared.core.extensions.*
import com.maproductions.mohamedalaa.shared.presentation.base.MABaseFragment
import com.maproductions.mohamedalaa.shared.presentation.myAccount.ItemUri
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import kotlin.math.roundToInt

@AndroidEntryPoint
class PreviousWorksFragment : MABaseFragment<FragmentPreviousWorksBinding>() {

    private val viewModel by viewModels<PreviousWorksViewModel>()

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
                requireContext().showNormalToast(getString(com.maproductions.mohamedalaa.shared.R.string.you_didn_t_accept_permission))
            }
        }
    }

    private val activityResultImageCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK) {
            val bitmap = it.data?.extras?.get("data") as? Bitmap ?: return@registerForActivityResult

            val uri = bitmap.getUriFromBitmapRetrievedByCamera(requireContext())

            val images = listOf(MAImage.IUri(uri))
            viewModel.images.value = viewModel.images.value.orEmpty() + images
            viewModel.adapter.addList(
                images.map { maImage -> ItemUri(-1, maImage) }
            )
        }
    }

    private val activityResultImageGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK) {
            val list = it.data?.clipData?.let { clipData ->
                List(clipData.itemCount) { index ->
                    clipData.getItemAt(index).uri
                }.filterNotNull()
            }.orIfNullOrEmpty {
                listOfNotNull(it.data?.data)
            }

            if (list.isEmpty()) {
                return@registerForActivityResult
            }

            if (list.size + viewModel.adapter.itemCount > 12) {
                context?.showNormalToast(getString(com.maproductions.mohamedalaa.shared.R.string.only_12_have_been_picked))
            }

            val amountToTake = 12 - viewModel.adapter.itemCount

            if (amountToTake > 0) {
                val images = list.take(amountToTake).map { uri -> MAImage.IUri(uri) }
                viewModel.images.value = viewModel.images.value.orEmpty() + images
                viewModel.adapter.addList(
                    images.map { maImage -> ItemUri(-1, maImage) }
                )
            }
        }
    }

    private val permissionVideoRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions[Manifest.permission.READ_EXTERNAL_STORAGE] == true
                    && permissions[Manifest.permission.WRITE_EXTERNAL_STORAGE] == true
                    && permissions[Manifest.permission.CAMERA] == true -> {
                pickVideoViaChooser()
            }
            permissions[Manifest.permission.READ_EXTERNAL_STORAGE] == true
                    && permissions[Manifest.permission.WRITE_EXTERNAL_STORAGE] == true -> {
                pickVideo(false)
            }
            permissions[Manifest.permission.CAMERA] == true -> {
                pickVideo(true)
            }
            else -> {
                requireContext().showNormalToast(getString(com.maproductions.mohamedalaa.shared.R.string.you_didn_t_accept_permission))
            }
        }
    }

    private val activityResultVideoCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK) {
            val uri = it.data?.data ?: return@registerForActivityResult

            if (uri.checkSizeAndLengthOfVideo(context ?: return@registerForActivityResult)) {
                viewModel.videoMAImage.value = MAImage.IUri(uri)
            }else {
                context?.showErrorToast(getString(SR.string.max_video_hint))
            }
        }
    }

    private val activityResultVideoGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK) {
            val uri = it.data?.data ?: return@registerForActivityResult

            if (uri.checkSizeAndLengthOfVideo(context ?: return@registerForActivityResult)) {
                viewModel.videoMAImage.value = MAImage.IUri(uri)
            }else {
                context?.showErrorToast(getString(SR.string.max_video_hint))
            }
        }
    }

    override fun getLayoutId(): Int = R.layout.fragment_previous_works

    override fun initializeBindingVariables() {
        binding?.viewModel = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding?.recyclerView?.layoutManager = object : StaggeredGridLayoutManager(2, VERTICAL) {
            override fun checkLayoutParams(lp: RecyclerView.LayoutParams?): Boolean {
                val position = kotlin.runCatching { lp?.absoluteAdapterPosition }.getOrNull()

                val context = context

                if (context != null && position != null && lp is LayoutParams) {
                    val actualPosition = if (position < 5) position else position % 5

                    lp.height = (when (actualPosition) {
                        0 -> 240
                        else -> 120
                    }).let {
                        context.dpToPx(it.toFloat()).roundToInt()
                    }
                }

                return super.checkLayoutParams(lp)
            }
        }
        binding?.recyclerView?.adapter = viewModel.adapter

        handleRetryAbleFlowWithMustHaveResultWithNullability(viewModel.retryAbleFlowPreviousWorkData) { maBaseResponse ->
            val response = maBaseResponse.data ?: return@handleRetryAbleFlowWithMustHaveResultWithNullability

            viewModel.buttonText.value = if (response.description.isEmpty() && response.files.isEmpty()) {
                getString(R.string.save)
            }else {
                getString(R.string.save_changes)
            }

            viewModel.showDescFirstLook.value = response.description.isEmpty()
            viewModel.description.value = response.description

            val videoItemFile = response.files.firstOrNull { it.fileType == FileType.VIDEO }
            viewModel.videoMAImage.value = videoItemFile?.fileUrl?.let { MAImage.ILink(it) }

            viewModel.showImagesFirstLook.value = response.files.isEmpty()

            val imagesFiles = response.files.filter { it.fileType == FileType.IMAGE }
            val images = imagesFiles.map { ItemUri(it.id, MAImage.ILink(it.fileUrl)) }
            viewModel.images.value = images.map { it.maImage }
            viewModel.adapter.submitList(images)
        }
    }

    fun pickVideoOrRequestPermissions() {
        when {
            requireContext().checkSelfPermissionGranted(Manifest.permission.READ_EXTERNAL_STORAGE)
                    && requireContext().checkSelfPermissionGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    && requireContext().checkSelfPermissionGranted(Manifest.permission.CAMERA) -> {
                pickVideoViaChooser()
            }
            else -> {
                permissionVideoRequest.launch(arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA,
                ))
            }
        }
    }

    private fun pickVideoViaChooser() {
        val camera = getString(com.maproductions.mohamedalaa.shared.R.string.camera)
        val gallery = getString(com.maproductions.mohamedalaa.shared.R.string.gallery)

        val view = if (viewModel.videoMAImage.value == null) {
            binding?.videoValueTextView
        }else {
            binding?.alreadyExistsVideoMaterialCardView
        }

        view?.showPopup(listOf(camera, gallery)) {
            pickVideo(it.title?.toString() == camera)
        }
    }

    private fun pickVideo(fromCamera: Boolean) {
        if (fromCamera) {
            activityResultVideoCamera.launch(Intent(MediaStore.ACTION_VIDEO_CAPTURE))
        }else {
            // From gallery
            activityResultVideoGallery.launch(
                Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI).also {
                    it.type = "video/*"
                }
            )
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

    private fun pickImage(fromCamera: Boolean) {
        if (fromCamera) {
            activityResultImageCamera.launch(Intent(MediaStore.ACTION_IMAGE_CAPTURE))
        }else {
            // From gallery
            /*
            For multiple selection see -> https://stackoverflow.com/a/47023265
             */
            val intent = Intent(Intent.ACTION_GET_CONTENT).also {
                it.type = "image/*"
                it.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            }
            activityResultImageGallery.launch(intent)
        }
    }

    private fun pickImageViaChooser() {
        val camera = getString(com.maproductions.mohamedalaa.shared.R.string.camera)
        val gallery = getString(com.maproductions.mohamedalaa.shared.R.string.gallery)

        binding?.imagesValueTextView?.showPopup(listOf(camera, gallery)) {
            pickImage(it.title?.toString() == camera)
        }
    }

}
