package com.example.musicplayer.view.fragment

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.SeekBar
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
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlaySongBinding.inflate(inflater, container, false)

        if (activity != null) {
            LocalBroadcastManager.getInstance(requireActivity()).registerReceiver(
                mBroadcastReceiver,
                IntentFilter(Constant.CHANGE_LISTENER)
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
        }
    }
    private fun initControl() {
        timeCalculator = Timer()

        binding?.imgPrevious?.setOnClickListener(this)
        binding?.imgPlay?.setOnClickListener(this)
        binding?.imgNext?.setOnClickListener(this)

        binding?.seekbar?.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onStopTrackingTouch(seekBar: SeekBar) {
                MusicService.mediaPlayer?.seekTo(seekBar.progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}

            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {}
        })
    }
    private fun showInfoSong() {
        if (MusicService.listSongPlaying.isEmpty()) {
            return
        }
        val currentSong = MusicService.listSongPlaying[MusicService.songPosition]
        binding?.tvSongName?.text = currentSong.title
        binding?.tvArtist?.text = currentSong.artist
        binding?.imgSong?.let {
            Glide.with(requireContext()).load(currentSong.image.toString()).into(
                it
            )
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
            binding?.imgSong?.animate()
                ?.rotationBy(360f)
                ?.withEndAction(this@PlaySongFragment::startAnimationPlayMusic)
                ?.setDuration(15000)
                ?.setInterpolator(LinearInterpolator())
                ?.start()
        }
        binding?.imgSong?.animate()
            ?.rotationBy(360f)
            ?.withEndAction(runnable)
            ?.setDuration(15000)
            ?.setInterpolator(LinearInterpolator())
            ?.start()
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
                    binding?.tvTimeCurrent?.text = getTime(MusicService.mediaPlayer!!.currentPosition)
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
        GlobalFunction.startMusicService(requireContext(), Constant.PREVIOUS, MusicService.songPosition)
    }
    private fun clickOnNextButton() {
        GlobalFunction.startMusicService(requireContext(), Constant.NEXT, MusicService.songPosition)
    }
    private fun clickOnPlayButton() {
        if (MusicService.mediaPlayer?.isPlaying == true) {
            GlobalFunction.startMusicService(requireContext(), Constant.PAUSE, MusicService.songPosition)
        } else {
            GlobalFunction.startMusicService(requireContext(), Constant.RESUME, MusicService.songPosition)
        }
    }
    fun getTime(millis: Int): String {
        val second = (millis / 1000 % 60).toLong()
        val minute = (millis / (1000 * 60)).toLong()
        return String.format("%02d:%02d", minute, second)
    }
}