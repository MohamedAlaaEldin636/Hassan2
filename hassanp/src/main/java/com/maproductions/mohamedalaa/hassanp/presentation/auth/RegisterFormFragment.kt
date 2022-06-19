package com.maproductions.mohamedalaa.hassanp.presentation.auth

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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.paging.CombinedLoadStates
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.maproductions.mohamedalaa.hassanp.R
import com.maproductions.mohamedalaa.shared.R as SR
import com.maproductions.mohamedalaa.hassanp.databinding.FragmentRegisterFormBinding
import com.maproductions.mohamedalaa.hassanp.presentation.auth.viewModel.RegisterFormViewModel
import com.maproductions.mohamedalaa.shared.core.customTypes.LocationData
import com.maproductions.mohamedalaa.shared.core.extensions.*
import com.maproductions.mohamedalaa.shared.domain.auth.ProviderData
import com.maproductions.mohamedalaa.shared.domain.auth.ResponseVerifyCode
import com.maproductions.mohamedalaa.shared.domain.location.LocationSelectionApiAction
import com.maproductions.mohamedalaa.shared.presentation.auth.VerifyPhoneFragment
import com.maproductions.mohamedalaa.shared.presentation.base.MABaseFragment
import com.maproductions.mohamedalaa.shared.presentation.location.LocationSelectionFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class RegisterFormFragment : MABaseFragment<FragmentRegisterFormBinding>() {

    private val viewModel by viewModels<RegisterFormViewModel>()

    @Inject
    lateinit var gson: Gson

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

    private val activityResultImageCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK) {
            val bitmap = it.data?.extras?.get("data") as? Bitmap ?: return@registerForActivityResult

            val uri = getUriFromBitmapRetrievedByCamera(bitmap)

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

    private val activityResultVideoCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK) {
            val uri = it.data?.data ?: return@registerForActivityResult

            viewModel.video.value = uri
        }
    }

    private val activityResultVideoGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK) {
            val uri = it.data?.data ?: return@registerForActivityResult

            viewModel.video.value = uri
        }
    }

    override fun getLayoutId(): Int = R.layout.fragment_register_form

    override fun initializeBindingVariables() {
        binding?.viewModel = viewModel
    }

    private val loadStateListener: (CombinedLoadStates) -> Unit = {
        if (viewModel.selectedCategoryId.value == null) {
            viewModel.selectedCategoryId.value = viewModel.adapterCategories.snapshot().items
                .firstOrNull()?.id
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding?.pickProfessionRecyclerView?.layoutManager = LinearLayoutManager(
            requireContext(), LinearLayoutManager.VERTICAL, false
        )
        binding?.pickProfessionRecyclerView?.adapter = viewModel.adapterCategories
            .withDefaultHeaderAndFooterAdapters()

        binding?.servicesCitiesRecyclerView?.layoutManager = LinearLayoutManager(
            requireContext(), LinearLayoutManager.VERTICAL, false
        )
        binding?.servicesCitiesRecyclerView?.adapter = viewModel.adapterServices
            .withDefaultHeaderAndFooterAdapters()

        viewModel.adapterCategories.addLoadStateListener(loadStateListener)

        viewModel.selectedCategoryId.distinctUntilChanged().observe(viewLifecycleOwner) { newValue ->
            if (newValue != null) {
                viewModel.adapterCategories.removeLoadStateListener(loadStateListener)

                viewModel.adapterServices.clearSelection()
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.categories.collectLatest {
                        viewModel.adapterCategories.submitData(it)
                    }
                }
                launch {
                    viewModel.services.collectLatest {
                        viewModel.adapterServices.submitData(it)
                    }
                }
            }
        }

        findNavController().currentBackStackEntry?.savedStateHandle?.actOnGetIfNotInitialValueOrGetLiveData(
            VerifyPhoneFragment.SAVED_STATE_VERIFICATION_RESPONSE_AS_JSON,
            "",
            viewLifecycleOwner,
            { !it.isNullOrEmpty() }
        ) {
            val response = it.fromJson<ResponseVerifyCode>(gson)

            lifecycleScope.launch {
                prefsAccount.setProviderData(
                    ProviderData(
                        response.id.orZero(),
                        response.typeOfAccount.orEmpty(),
                        response.verified.orZero(),
                        name = response.name,
                        email = response.email,
                        phone = response.phone,
                        latitude = response.latitude,
                        longitude = response.longitude,
                        address = response.address,
                        apiToken = response.apiToken.orEmpty(),
                        imageUrl = response.imageUrl,
                        ordersFlag = response.ordersFlag.orZero(),
                        services = response.services,
                        category = response.category,
                    )
                )

                findNavControllerOfProject().navigateDeepLinkWithOptions(
                    "fragment-dest",
                    "com.grand.hassan.shared.location.selection.back.button.and.api.action",
                    paths = arrayOf(false.toString(), LocationSelectionApiAction.UPDATE_PROVIDER_PROFILE.name)
                )
            }
        }

        findNavController().currentBackStackEntry?.savedStateHandle?.actOnGetIfNotInitialValueOrGetLiveData(
            LocationSelectionFragment.KEY_FRAGMENT_RESULT_LOCATION_DATA_AS_JSON,
            "",
            viewLifecycleOwner,
            { !it.isNullOrEmpty() }
        ) {
            val locationData = it.fromJson<LocationData>(gson)

            lifecycleScope.launch {
                prefsAccount.setProviderData(
                    prefsAccount.getProviderData().first()!!.copy(
                        latitude = locationData.latitude,
                        longitude = locationData.longitude,
                        address = locationData.address,
                    )
                )

                val navController = findNavController()

                navController.navigateUp()

                navController.navigateDeepLinkWithoutOptions(
                    "dialog-dest",
                    "com.grand.hassan.shared.registration.done.dialog"
                )
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

    private fun pickVideoViaChooser() {
        val delete = if (viewModel.video.value == null) null else getString(SR.string.delete)
        val camera = getString(com.maproductions.mohamedalaa.shared.R.string.camera)
        val gallery = getString(com.maproductions.mohamedalaa.shared.R.string.gallery)

        binding?.videoFrameLayout?.showPopup(listOfNotNull(delete, camera, gallery)) {
            if (it.title?.toString() == delete) {
                viewModel.video.value = null
            }else {
                pickVideo(it.title?.toString() == camera)
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
            RegisterFormViewModel.ImageType.ADDRESS_FRONT -> binding?.addressFrontFrameLayout
            RegisterFormViewModel.ImageType.ADDRESS_BACK -> binding?.addressBackFrameLayout
        }
        view?.showPopup(listOf(camera, gallery)) {
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
