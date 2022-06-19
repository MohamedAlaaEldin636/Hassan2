package com.maproductions.mohamedalaa.hassanp.presentation.myAccount.viewModel

import android.app.Application
import android.view.View
import androidx.fragment.app.findFragment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.maproductions.mohamedalaa.hassanp.presentation.myAccount.PreviousWorksFragment
import com.maproductions.mohamedalaa.hassanp.presentation.myAccount.adapter.RVItemImageDel2
import com.maproductions.mohamedalaa.shared.core.customTypes.MAImage
import com.maproductions.mohamedalaa.shared.core.customTypes.RetryAbleFlow
import com.maproductions.mohamedalaa.shared.core.extensions.*
import com.maproductions.mohamedalaa.shared.data.api.ApiConst
import com.maproductions.mohamedalaa.shared.data.myAccount.repository.RepoMyAccount
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PreviousWorksViewModel @Inject constructor(
    application: Application,
    private val repoMyAccount: RepoMyAccount,
    gson: Gson,
) : AndroidViewModel(application), RVItemImageDel2.Listener {

    val retryAbleFlowPreviousWorkData = RetryAbleFlow {
        repoMyAccount.getPreviousWorkData()
    }

    val description = MutableLiveData<String?>()
    val showDescFirstLook = MutableLiveData(true)

    val videoMAImage = MutableLiveData<MAImage?>()
    val showVideoFirstLook = videoMAImage.map {
        it == null
    }

    val images = MutableLiveData<List<MAImage>>(emptyList())
    val showImagesFirstLook = MutableLiveData(true)
    val showImagesPicker = images.map {
        it?.size.orZero() < 12
    }

    val adapter = RVItemImageDel2(this, gson)

    val buttonText = MutableLiveData("")

    fun pickOrClearVideo(view: View) {
        view.findFragment<PreviousWorksFragment>().pickVideoOrRequestPermissions()
    }

    fun showVideo(view: View) {
        val maImage = videoMAImage.value ?: return

        view.findNavControllerOfProject().navigateDeepLinkWithoutOptions(
            "dialog-dest",
            "com.grand.hassan.shared.video.player.dialog.is.uri",
            maImage.getId(),
            (maImage is MAImage.IUri).toString()
        )
    }

    fun pickImages(view: View) {
        view.findFragment<PreviousWorksFragment>().pickImageOrRequestPermissions()
    }

    fun save(view: View) {
        val fragment = view.findFragment<PreviousWorksFragment>()

        fragment.executeOnGlobalLoadingAndAutoHandleRetryCancellable2(
            afterShowingLoading = {
                repoMyAccount.addOrUpdatePreviousWorkData(
                    description.value.orEmpty(),
                    images.value.orEmpty().filterIsInstance<MAImage.IUri>().mapNotNull {
                        it.uri.createMultipartBodyPart(view.context, ApiConst.Query.IMAGES + "[]")
                    },
                    (videoMAImage.value as? MAImage.IUri)?.uri?.createMultipartBodyPart(
                        view.context, ApiConst.Query.VIDEO
                    )
                )
            },
            afterHidingLoading = {
                view.context.showSuccessToast(it.message)

                fragment.findNavControllerOfProject().navigateUp()
            }
        )
    }

    override fun delete(view: View, id: Int, maImageId: String) {
        if (id >= 0) {
            val fragment = view.findFragment<PreviousWorksFragment>()

            fragment.executeOnGlobalLoadingAndAutoHandleRetryCancellable(
                afterShowingLoading = {
                    repoMyAccount.deleteImageOrVideoOfPreviousWorkData(id)
                },
                afterHidingLoading = {
                    performDeletion(maImageId)
                }
            )
        }else {
            performDeletion(maImageId)
        }
    }

    private fun performDeletion(maImageId: String) {
        val newList = adapter.getList().toMutableList()
        newList.removeAll { it.maImage.getId() == maImageId }

        images.value = newList.map { it.maImage }
        adapter.submitList(newList.toList())
    }

}