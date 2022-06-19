package com.maproductions.mohamedalaa.shared.presentation.base

import android.annotation.SuppressLint
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.view.View
import android.view.Window
import androidx.navigation.fragment.navArgs
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.maproductions.mohamedalaa.shared.R
import com.maproductions.mohamedalaa.shared.databinding.DialogFragmentVideoPlayerBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class VideoPlayerDialogFragment : MADialogFragment<DialogFragmentVideoPlayerBinding>() {

    override val heightIsMatchParent = true

    override fun getLayoutId(): Int = R.layout.dialog_fragment_video_player

    private val args by navArgs<VideoPlayerDialogFragmentArgs>()

    private var player: ExoPlayer? = null

    private var playWhenReady = true
    private var currentWindow = 0
    private var playbackPosition = 0L

    override fun onCreateDialogWindowChanges(window: Window) {
        window.setBackgroundDrawable(ColorDrawable(requireContext().getColor(R.color.black_alpha_50)))

        window.attributes?.windowAnimations = R.style.ScaleDialogAnim
    }

    override fun onStart() {
        super.onStart()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            initializePlayer()
        }
    }

    override fun onResume() {
        super.onResume()

        hideSystemUi()

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N || player == null) {
            initializePlayer()
        }
    }

    override fun onPause() {
        super.onPause()

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            releasePlayer()
        }
    }

    override fun onStop() {
        super.onStop()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            releasePlayer()
        }
    }

    private fun initializePlayer() {
        player = /*Simple*/ExoPlayer.Builder(requireContext())
            .build()
            .also { exoPlayer ->
                binding?.playerView?.player = exoPlayer

                val mediaItem = if (args.isUri) {
                    MediaItem.fromUri(Uri.parse(args.url))
                }else {
                    MediaItem.fromUri(args.url)
                }
                exoPlayer.setMediaItem(mediaItem)

                exoPlayer.playWhenReady = playWhenReady
                exoPlayer.seekTo(currentWindow, playbackPosition)
                exoPlayer.prepare()
            }
    }

    private fun releasePlayer() {
        player?.run {
            playbackPosition = this.currentPosition
            currentWindow = this.currentMediaItemIndex // currentWindowIndex
            this@VideoPlayerDialogFragment.playWhenReady = this.playWhenReady

            release()
        }
        player = null
    }

    @SuppressLint("InlinedApi")
    private fun hideSystemUi() {
        // todo use the not deprecated one -> WindowInsetsController
        /*val mainContainer = (activity as? ProjectActivity)?.mainContainer
        if (mainContainer != null) {
            WindowCompat.setDecorFitsSystemWindows(requireActivity().window, false)
            WindowInsetsControllerCompat(requireActivity().window, mainContainer).let { controller ->
                controller.hide(WindowInsetsCompat.Type.systemBars())
                controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        }*/

        binding?.playerView?.systemUiVisibility = (View.SYSTEM_UI_FLAG_LOW_PROFILE
            or View.SYSTEM_UI_FLAG_FULLSCREEN
            or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
    }
}
