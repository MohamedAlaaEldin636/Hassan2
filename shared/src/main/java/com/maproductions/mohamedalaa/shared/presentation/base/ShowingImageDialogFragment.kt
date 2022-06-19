package com.maproductions.mohamedalaa.shared.presentation.base

import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.InsetDrawable
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.navArgs
import androidx.transition.ChangeBounds
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.maproductions.mohamedalaa.shared.R
import com.maproductions.mohamedalaa.shared.core.extensions.dpToPx
import com.maproductions.mohamedalaa.shared.databinding.ItemImageBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.roundToInt

@AndroidEntryPoint
class ShowingImageDialogFragment : MADialogFragment<ItemImageBinding>() {
	
	override val heightIsMatchParent = true
	
	override fun getLayoutId(): Int = R.layout.item_image
	
	private val args by navArgs<ShowingImageDialogFragmentArgs>()
	
	override fun onCreateDialogWindowChanges(window: Window) {
		val drawable = InsetDrawable(
			ContextCompat.getDrawable(requireContext(), R.drawable.dr_rounded_white_medium),
			requireContext().dpToPx(16f).roundToInt()
		)
		window.setBackgroundDrawable(drawable)
		
		window.attributes?.windowAnimations = R.style.ScaleDialogAnim
	}
	
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		binding?.imageView?.scaleType = ImageView.ScaleType.FIT_CENTER
		if (args.isUri) {
			Glide.with(this)
				.load(Uri.parse(args.imageUrl))
				.apply(RequestOptions().fitCenter())
				.placeholder(R.drawable.ic_logo_app_placeholder)
				.error(R.drawable.ic_logo_app_placeholder)
				.into(binding?.imageView ?: return)
		}else {
			Glide.with(this)
				.load(args.imageUrl)
				.apply(RequestOptions().fitCenter())
				.placeholder(R.drawable.ic_logo_app_placeholder)
				.error(R.drawable.ic_logo_app_placeholder)
				.into(binding?.imageView ?: return)
		}
	}
	
}
