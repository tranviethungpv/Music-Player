package com.example.musicplayer.view.fragment

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.SeekBar
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.bumptech.glide.Glide
import com.example.musicplayer.Constant
import com.example.musicplayer.GlobalFunction
import com.example.musicplayer.R
import com.example.musicplayer.databinding.FragmentPlaySongBinding
import com.example.musicplayer.service.MusicService
import java.util.Timer
import java.util.TimerTask

class PlaySongFragment : Fragment(), View.OnClickListener {
    private var binding: FragmentPlaySongBinding? = null
    private var timeCalculator: Timer? = null
    private val mBroadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            MusicService.songAction = intent.getIntExtra(Constant.MUSIC_ACTION, 0)
            handleMusicAction()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlaySongBinding.inflate(inflater, container, false)
        MusicService.repeatMode = Constant.REPEAT_ALL
        MusicService.isShuffle = false
        if (activity != null) {
            LocalBroadcastManager.getInstance(requireActivity()).registerReceiver(
                mBroadcastReceiver, IntentFilter(Constant.CHANGE_LISTENER)
            )
        }
        initControl()
        showInfoSong()
        MusicService.songAction = MusicService.songAction
        handleMusicAction()

        return binding?.root
    }

    override fun onDestroy() {
        super.onDestroy()
        timeCalculator?.cancel()
        timeCalculator = null
        activity?.let {
            LocalBroadcastManager.getInstance(it).unregisterReceiver(mBroadcastReceiver)
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.img_previous -> clickOnPrevButton()
            R.id.img_play -> clickOnPlayButton()
            R.id.img_next -> clickOnNextButton()
            R.id.img_repeat -> clickOnRepeatButton()
            R.id.img_shuffle -> clickOnShuffleButton()
        }
    }

    private fun initControl() {
        timeCalculator = Timer()

        binding?.imgPrevious?.setOnClickListener(this)
        binding?.imgPlay?.setOnClickListener(this)
        binding?.imgNext?.setOnClickListener(this)
        binding?.imgShuffle?.setOnClickListener(this)
        binding?.imgRepeat?.setOnClickListener(this)
        binding?.seekbar?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onStopTrackingTouch(seekBar: SeekBar) {
                MusicService.mediaPlayer?.seekTo(seekBar.progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}

            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {}
        })
    }

    private fun showInfoSong() {
        if (MusicService.currentListSong.isEmpty()) {
            return
        }
        if (MusicService.repeatMode != Constant.REPEAT_ONE) {
            val currentSong = MusicService.currentListSong[MusicService.songPosition]
            binding?.tvSongName?.text = currentSong.title
            binding?.tvArtist?.text = currentSong.artist
            binding?.imgSong?.let {
                Glide.with(requireContext()).load(currentSong.image.toString()).into(
                    it
                )
            }
        }
    }

    @Suppress("DEPRECATION")
    private fun handleMusicAction() {
        if (Constant.CANCEL_NOTIFICATION == MusicService.songAction) {
            requireActivity().onBackPressed()
            return
        }
        when (MusicService.songAction) {
            Constant.PREVIOUS, Constant.NEXT -> {
                stopAnimationPlayMusic()
                showInfoSong()
            }

            Constant.PLAY -> {
                showInfoSong()
                if (MusicService.isPlaying) {
                    startAnimationPlayMusic()
                }
                showSeekBar()
                showStatusButtonPlay()
            }

            Constant.PAUSE -> {
                stopAnimationPlayMusic()
                showSeekBar()
                showStatusButtonPlay()
            }

            Constant.RESUME -> {
                startAnimationPlayMusic()
                showSeekBar()
                showStatusButtonPlay()
            }
        }
    }

    private fun startAnimationPlayMusic() {
        val runnable = Runnable {
            binding?.imgSong?.animate()?.rotationBy(360f)
                ?.withEndAction(this@PlaySongFragment::startAnimationPlayMusic)?.setDuration(15000)
                ?.setInterpolator(LinearInterpolator())?.start()
        }
        binding?.imgSong?.animate()?.rotationBy(360f)?.withEndAction(runnable)?.setDuration(15000)
            ?.setInterpolator(LinearInterpolator())?.start()
    }

    private fun stopAnimationPlayMusic() {
        binding?.imgSong?.animate()?.cancel()
    }

    private fun showSeekBar() {
        Timer().schedule(object : TimerTask() {
            override fun run() {
                activity?.runOnUiThread {
                    if (MusicService.mediaPlayer == null) {
                        return@runOnUiThread
                    }
                    binding?.tvTimeCurrent?.text =
                        getTime(MusicService.mediaPlayer!!.currentPosition)
                    binding?.tvTimeMax?.text = getTime(MusicService.lengthSong)
                    binding?.seekbar?.max = MusicService.lengthSong
                    binding?.seekbar?.progress = MusicService.mediaPlayer!!.currentPosition
                }
            }
        }, 0, 1000)
    }

    private fun showStatusButtonPlay() {
        if (MusicService.isPlaying) {
            binding?.imgPlay?.setImageResource(R.drawable.ic_pause_black)
        } else {
            binding?.imgPlay?.setImageResource(R.drawable.ic_play_black)
        }
    }

    private fun clickOnPrevButton() {
        GlobalFunction.processForShuffle()
        GlobalFunction.startMusicService(
            requireContext(), Constant.PREVIOUS, MusicService.songPosition
        )
    }

    private fun clickOnNextButton() {
        GlobalFunction.processForShuffle()
        GlobalFunction.startMusicService(requireContext(), Constant.NEXT, MusicService.songPosition)
    }

    private fun clickOnPlayButton() {
        if (MusicService.mediaPlayer?.isPlaying == true) {
            GlobalFunction.startMusicService(
                requireContext(), Constant.PAUSE, MusicService.songPosition
            )
        } else {
            GlobalFunction.startMusicService(
                requireContext(), Constant.RESUME, MusicService.songPosition
            )
        }
    }

    private fun clickOnShuffleButton() {
        if (!MusicService.isShuffle) {
            MusicService.shuffleMusic(requireContext())
            Toast.makeText(requireContext(), "Shuffle On!", Toast.LENGTH_SHORT).show()
        } else {
            MusicService.shuffleMusic(requireContext())
            Toast.makeText(requireContext(), "Shuffle Off!", Toast.LENGTH_SHORT).show()
        }
        binding?.imgShuffle?.setColorFilter(
            ContextCompat.getColor(
                requireContext(), if (MusicService.isShuffle) R.color.black else R.color.gray
            ), PorterDuff.Mode.SRC_IN
        )
    }

    private fun clickOnRepeatButton() {
        when (MusicService.repeatMode) {
            Constant.REPEAT_NONE -> {
                MusicService.repeatMode = Constant.REPEAT_ALL
                binding?.imgRepeat?.setColorFilter(
                    ContextCompat.getColor(
                        requireContext(), R.color.black
                    ), PorterDuff.Mode.SRC_IN
                )
                Toast.makeText(requireContext(), "Repeat All!", Toast.LENGTH_SHORT).show()
            }

            Constant.REPEAT_ALL -> {
                MusicService.repeatMode = Constant.REPEAT_ONE
                binding?.imgRepeat?.setImageResource(R.drawable.img_repeat_one_black)
                binding?.imgRepeat?.setColorFilter(
                    ContextCompat.getColor(
                        requireContext(), R.color.black
                    ), PorterDuff.Mode.SRC_IN
                )
                Toast.makeText(requireContext(), "Repeat One!", Toast.LENGTH_SHORT).show()
            }

            Constant.REPEAT_ONE -> {
                MusicService.repeatMode = Constant.REPEAT_NONE
                binding?.imgRepeat?.setImageResource(R.drawable.img_repeat_black)
                binding?.imgRepeat?.setColorFilter(
                    ContextCompat.getColor(
                        requireContext(), R.color.gray
                    ), PorterDuff.Mode.SRC_IN
                )
                Toast.makeText(requireContext(), "Not Repeat!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun getTime(millis: Int): String {
        val second = (millis / 1000 % 60).toLong()
        val minute = (millis / (1000 * 60)).toLong()
        return String.format("%02d:%02d", minute, second)
    }
}