package com.maproductions.mohamedalaa.hassanu.presentation.settings.viewModel

import android.app.Application
import android.graphics.Typeface
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.View
import androidx.core.text.buildSpannedString
import androidx.core.text.set
import androidx.fragment.app.findFragment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.maproductions.mohamedalaa.hassanu.presentation.settings.GetDiscountsFragment
import com.maproductions.mohamedalaa.hassanu.presentation.settings.GetDiscountsFragmentDirections
import com.maproductions.mohamedalaa.shared.core.customTypes.RetryAbleFlow
import com.maproductions.mohamedalaa.shared.core.extensions.*
import com.maproductions.mohamedalaa.shared.data.codes.repository.RepoCodes
import com.maproductions.mohamedalaa.shared.R as SR
import com.maproductions.mohamedalaa.shared.data.settings.repository.RepoSettings
import com.maproductions.mohamedalaa.shared.domain.settings.ImageWithTextAndTitleFlag
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.math.roundToInt

@HiltViewModel
class GetDiscountsViewModel @Inject constructor(
    application: Application,
    repoSettings: RepoSettings,
    private val repoCodes: RepoCodes,
) : AndroidViewModel(application) {

    val retryAbleFlow = RetryAbleFlow(repoSettings::getPoints)

    val text2 by lazy {
        buildSpannedString {
            append(myApp.getString(SR.string.get_discount_2))

            val coloredPart = myApp.getString(SR.string.get_discount_2_specific_part)

            val startIndex = indexOf(coloredPart)
            val endIndex = startIndex + coloredPart.length

            this[startIndex..endIndex] = ForegroundColorSpan(myApp.getColor(SR.color.coloredText))
        }
    }

    val text3 = MutableLiveData(myApp.getString(SR.string.get_discount_3))

    val text4 by lazy {
        buildSpannedString {
            append(myApp.getString(SR.string.get_discount_4))

            val coloredPart = myApp.getString(SR.string.get_discount_4_specific_part)

            val startIndex = indexOf(coloredPart)
            val endIndex = startIndex + coloredPart.length

            this[startIndex..endIndex] = ForegroundColorSpan(myApp.getColor(SR.color.coloredText))
            this[startIndex..endIndex] = StyleSpan(Typeface.BOLD)
            this[startIndex..endIndex] = createClickableSpan { toTermsAndConditions(it) }
        }
    }

    val yourLink = MutableLiveData("")

    val myPoints = MutableLiveData<Int>()

    val gainedPoints = myPoints.map {
        myApp.getString(SR.string.num_point, it.orZero())
    }

    private fun toTermsAndConditions(view: View) {
        view.findNavControllerOfProject().navigateDeepLinkWithOptions(
            "fragment-dest",
            "com.grand.hassan.shared.image.with.text.and.title",
            paths = arrayOf(ImageWithTextAndTitleFlag.USER_TERMS_AND_CONDITIONS.name)
        )
    }

    fun copyYourLink(view: View) {
        view.context.copyToClipboard(yourLink.value.orEmpty())
    }

    fun toGetDiscountCodeDialog(view: View) {
        if (myPoints.value == null || myPoints.value == 0) {
            return view.context.showErrorToast(view.context.getString(SR.string.you_have_no_points))
        }

        val fragment = view.findFragment<GetDiscountsFragment>()

        fragment.executeOnGlobalLoadingAndAutoHandleRetryCancellable(
            afterShowingLoading = {
                repoCodes.replacePoints()
            },
            afterHidingLoading = {
                myPoints.value = myPoints.value.orZero().minus(
                    it?.points?.toFloatOrNull()?.roundToInt().orZero()
                ).coerceAtLeast(0)

                fragment.findNavControllerOfProject().navigate(
                    GetDiscountsFragmentDirections.actionDestGetDiscountsToDestGetDiscountCodeDialog(
                        it?.code.orEmpty(),
                        it?.points?.toFloatOrNull()?.roundToInt().orZero()
                    )
                )
            }
        )
    }

    fun shareYourLink(view: View) {
        view.context.launchShareText(yourLink.value.orEmpty())
    }

}
