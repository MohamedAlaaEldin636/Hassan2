package com.maproductions.mohamedalaa.hassanu.presentation.service

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.maproductions.mohamedalaa.hassanu.R
import com.maproductions.mohamedalaa.shared.R as SR
import com.maproductions.mohamedalaa.hassanu.databinding.FragmentServiceImageAndDescriptionSelectionBinding
import com.maproductions.mohamedalaa.hassanu.presentation.service.viewModel.ServiceImageAndDescriptionSelectionViewModel
import com.maproductions.mohamedalaa.shared.core.extensions.checkSelfPermissionGranted
import com.maproductions.mohamedalaa.shared.core.extensions.orIfNullOrEmpty
import com.maproductions.mohamedalaa.shared.core.extensions.showNormalToast
import com.maproductions.mohamedalaa.shared.core.extensions.showPopup
import com.maproductions.mohamedalaa.shared.presentation.base.MABaseFragment
import dagger.hilt.android.AndroidEntryPoint
import java.io.ByteArrayOutputStream
import java.util.*

@AndroidEntryPoint
class ServiceImageAndDescriptionSelectionFragment : MABaseFragment<FragmentServiceImageAndDescriptionSelectionBinding>() {

    private val viewModel by viewModels<ServiceImageAndDescriptionSelectionViewModel>()

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

            val uri = getUriFromBitmapRetrievedByCamera(bitmap)

            viewModel.adapter.addItemsUri(listOf(uri))
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

            if (list.size > 12) {
                context?.showNormalToast(getString(SR.string.only_12_have_been_picked))
            }

            viewModel.adapter.addItemsUri(list.take(12))
        }
    }

    override fun getLayoutId(): Int = R.layout.fragment_service_image_and_description_selection

    override fun initializeBindingVariables() {
        binding?.viewModel = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        activityViewModel?.titleToolbar?.postValue(viewModel.args.categoryName)

        binding?.recyclerView?.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        binding?.recyclerView?.adapter = viewModel.adapter
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

        binding?.imageView?.showPopup(listOf(camera, gallery)) {
            pickImage(it.title?.toString() == camera)
        }
    }

    private fun getUriFromBitmapRetrievedByCamera(bitmap: Bitmap): Uri {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream)
        val byteArray = stream.toByteArray()
        val compressedBitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)

        val path = MediaStore.Images.Media.insertImage(
            requireContext().contentResolver, compressedBitmap, Date(System.currentTimeMillis()).toString() + "photo", null
        )
        return Uri.parse(path)
    }

}
