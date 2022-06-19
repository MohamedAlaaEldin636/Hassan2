package com.maproductions.mohamedalaa.shared.core.extensions

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.annotation.NavigationRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.findFragment
import androidx.navigation.*
import com.maproductions.mohamedalaa.shared.NavSharedArgs
import com.maproductions.mohamedalaa.shared.R

fun Fragment.findNavControllerOfProject(): NavController {
	return Navigation.findNavController(
		requireActivity(),
		R.id.mainNavHostFragment
	)
}

fun View.findNavControllerOfProject(): NavController {
	return findFragment<Fragment>().findNavControllerOfProject()
}

fun NavController.popAllBackStacks() {
	while (popBackStack()) {
		continue
	}
}

fun NavController.navigateDeepLinkWithoutOptions(scheme: String, authority: String, vararg paths: String) =
	navigateDeepLinkOptionalOptions(scheme, authority, null, *paths)

fun defaultAnimationsNavOptionsBuilder() = NavOptions.Builder()
	.setEnterAnim(R.anim.anim_slide_in_right)
	.setExitAnim(R.anim.anim_slide_out_left)
	.setPopEnterAnim(R.anim.anim_slide_in_left)
	.setPopExitAnim(R.anim.anim_slide_out_right)

fun NavController.navigateDeepLinkWithOptions(
	scheme: String,
	authority: String,
	options: NavOptions = NavOptions.Builder()
		.setEnterAnim(R.anim.anim_slide_in_right)
		.setExitAnim(R.anim.anim_slide_out_left)
		.setPopEnterAnim(R.anim.anim_slide_in_left)
		.setPopExitAnim(R.anim.anim_slide_out_right)
		.build(),
	vararg paths: String?
) = navigateDeepLinkOptionalOptions(scheme, authority, options, *paths)

private fun NavController.navigateDeepLinkOptionalOptions(
	scheme: String,
	authority: String,
	options: NavOptions?,
	vararg paths: String?
) {
	val uri = Uri.Builder()
		.scheme(scheme)
		.authority(authority)
		.let {
			var builder = it
			for (path in paths) {
				builder = builder.appendPath(path)
			}
			builder
		}
		.build()
	val request = NavDeepLinkRequest.Builder.fromUri(uri).build()

	navigate(request, options)
}

fun NavController.inflateGraph(@NavigationRes navigationRes: Int, args: Bundle? = null, startDestinationId: Int = -1) {
	try {
		graph
	}catch (e: IllegalStateException) {
		val navGraph = navInflater.inflate(navigationRes)
		if (startDestinationId >= 0) {
			navGraph.setStartDestination(startDestinationId)
		}
		setGraph(navGraph, args)
	}
}

fun NavController.executeWhenLoadingEndsIfExists(block: () -> Unit) {
	if (getBackStackEntryOrNull(R.id.dest_lottie_loader_dialog) != null) {
		addOnDestinationChangedListener(object : NavController.OnDestinationChangedListener {
			override fun onDestinationChanged(
				controller: NavController,
				destination: NavDestination,
				arguments: Bundle?
			) {
				if (controller.getBackStackEntryOrNull(R.id.dest_lottie_loader_dialog) != null) {
					removeOnDestinationChangedListener(this)

					block()
				}
			}
		})
	}else {
		block()
	}
}

private fun NavController.getBackStackEntryOrNull(destinationId: Int) = kotlin.runCatching {
	getBackStackEntry(destinationId)
}.getOrNull()
