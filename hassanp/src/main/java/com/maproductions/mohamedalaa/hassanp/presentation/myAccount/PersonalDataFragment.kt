package com.maproductions.mohamedalaa.hassanp.presentation.myAccount

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.maproductions.mohamedalaa.hassanp.R
import com.maproductions.mohamedalaa.hassanp.databinding.FragmentPersonalDataBinding
import com.maproductions.mohamedalaa.hassanp.presentation.auth.viewModel.RegisterFormViewModel
import com.maproductions.mohamedalaa.hassanp.presentation.myAccount.viewModel.PersonalDataViewModel
import com.maproductions.mohamedalaa.shared.core.customTypes.MAImage
import com.maproductions.mohamedalaa.shared.core.customTypes.PermissionsHandler
import com.maproductions.mohamedalaa.shared.core.extensions.*
import com.maproductions.mohamedalaa.shared.presentation.base.MABaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PersonalDataFragment : MABaseFragment<FragmentPersonalDataBinding>(), PermissionsHandler.Listener {

    companion object {
        const val DIALOG_DATE_PICKER_KEY = "PersonalDataFragment.DIALOG_DATE_PICKER_KEY"
    }

    private val viewModel by viewModels<PersonalDataViewModel>()

    private val permissionsImageRequest = registerForActivityResult(
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

            viewModel.setImageUri(uri)
        }
    }

    private val activityResultImageGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK) {
            val uri = it.data?.data ?: return@registerForActivityResult

            viewModel.setImageUri(uri)
        }
    }

    override fun getLayoutId(): Int = R.layout.fragment_personal_data

    override fun initializeBindingVariables() {
        binding?.viewModel = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        handleRetryAbleFlowWithMustHaveResultWithNullability(viewModel.retryAbleFlow) { response ->
            viewModel.response = response.data

            viewModel.showIdsFields.value = response.data?.isSuspendedAccount != true
            viewModel.imageProfile.value = response.data?.imageUrl?.let {
                MAImage.ILink(it)
            }
            viewModel.name.value = response.data?.name
            viewModel.phone.value = response.data?.phone
            viewModel.birthDate.value = response.data?.birthDate?.split("-")?.reversed()
                ?.joinToString(" / ")

            if (response.data?.isSuspendedAccount == true) {
                val files = response.data?.files

                val profileFile = files?.firstOrNull {
                    RegisterFormViewModel.ImageType.fromProviderFileType(it.type) ==
                            RegisterFormViewModel.ImageType.PROFILE
                }
                val profileRequired = profileFile?.isStatusApproved?.not() ?: false
                val frontIdFile = files?.firstOrNull {
                    RegisterFormViewModel.ImageType.fromProviderFileType(it.type) ==
                            RegisterFormViewModel.ImageType.ID_FRONT
                }
                val frontIdRequired = frontIdFile?.isStatusApproved?.not() ?: false
                val backIdFile = files?.firstOrNull {
                    RegisterFormViewModel.ImageType.fromProviderFileType(it.type) ==
                            RegisterFormViewModel.ImageType.ID_BACK
                }
                val backIdRequired = backIdFile?.isStatusApproved?.not() ?: false

                viewModel.requireProfile.value = profileRequired
                viewModel.requireFrontId.value = frontIdRequired
                viewModel.requireBackId.value = backIdRequired

                viewModel.imageFrontId.value = frontIdFile?.fileUrl?.let { MAImage.ILink(it) }
                viewModel.imageBackId.value = backIdFile?.fileUrl?.let { MAImage.ILink(it) }
            }else {
                viewModel.requireProfile.value = false
                viewModel.requireFrontId.value = false
                viewModel.requireBackId.value = false
            }
        }
    }

    fun pickImageOrRequestPermissions() {
        when {
            requireContext().checkSelfPermissionGranted(Manifest.permission.READ_EXTERNAL_STORAGE)
                    && requireContext().checkSelfPermissionGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    && requireContext().checkSelfPermissionGranted(Manifest.permission.CAMERA) -> {
                pickImageViaChooser()
            }
            else -> {
                permissionsImageRequest.launch(arrayOf(
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
        val camera = getString(com.maproductions.mohamedalaa.shared.R.string.camera)
        val gallery = getString(com.maproductions.mohamedalaa.shared.R.string.gallery)

        val view = when (viewModel.imageType) {
            RegisterFormViewModel.ImageType.PROFILE -> binding?.imageMaterialCardView
            RegisterFormViewModel.ImageType.ID_FRONT -> binding?.idFrontFrameLayout
            RegisterFormViewModel.ImageType.ID_BACK -> binding?.backFrontFrameLayout
            else -> return
        }

        view?.showPopup(listOf(camera, gallery)) {
            pickImage(it.title?.toString() == camera)
        }
    }

}
