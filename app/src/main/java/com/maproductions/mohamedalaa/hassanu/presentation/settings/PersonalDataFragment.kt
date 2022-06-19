package com.maproductions.mohamedalaa.hassanu.presentation.settings

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
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.maproductions.mohamedalaa.hassanu.R
import com.maproductions.mohamedalaa.shared.R as SR
import com.maproductions.mohamedalaa.hassanu.databinding.FragmentFilterOrdersBinding
import com.maproductions.mohamedalaa.hassanu.databinding.FragmentPersonalDataBinding
import com.maproductions.mohamedalaa.hassanu.presentation.order.viewModel.FilterOrdersViewModel
import com.maproductions.mohamedalaa.hassanu.presentation.settings.viewModel.PersonalDataViewModel
import com.maproductions.mohamedalaa.shared.core.extensions.checkSelfPermissionGranted
import com.maproductions.mohamedalaa.shared.core.extensions.showNormalToast
import com.maproductions.mohamedalaa.shared.core.extensions.showPopup
import com.maproductions.mohamedalaa.shared.presentation.base.MABaseFragment
import dagger.hilt.android.AndroidEntryPoint
import java.io.ByteArrayOutputStream
import java.util.*

@AndroidEntryPoint
class PersonalDataFragment : MABaseFragment<FragmentPersonalDataBinding>() {

    private val permissionLocationRequest = registerForActivityResult(
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
                requireContext().showNormalToast(getString(SR.string.you_didn_t_accept_permission))
            }
        }
    }

    private val activityResultImageCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK) {
            val bitmap = it.data?.extras?.get("data") as? Bitmap ?: return@registerForActivityResult

            val uri = getUriFromBitmapRetrievedByCamera(bitmap)

            viewModel.imageUri = uri

            Glide.with(this)
                .load(uri)
                .apply(RequestOptions().centerCrop())
                .into(binding?.imageView ?: return@registerForActivityResult)
        }
    }

    private val activityResultImageGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK) {
            val uri = it.data?.data ?: return@registerForActivityResult

            viewModel.imageUri = uri

            Glide.with(this)
                .load(uri)
                .apply(RequestOptions().centerCrop())
                .into(binding?.imageView ?: return@registerForActivityResult)
        }
    }

    private val viewModel by viewModels<PersonalDataViewModel>()

    override fun getLayoutId(): Int = R.layout.fragment_personal_data

    override fun initializeBindingVariables() {
        binding?.viewModel = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

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
                permissionLocationRequest.launch(arrayOf(
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
            activityResultImageGallery.launch(Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI))
        }
    }

    private fun pickImageViaChooser() {
        val camera = getString(SR.string.camera)
        val gallery = getString(SR.string.gallery)

        binding?.imageMaterialCardView?.showPopup(listOf(camera, gallery)) {
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
