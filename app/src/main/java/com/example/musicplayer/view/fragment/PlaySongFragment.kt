package com.example.musicplayer.view.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.musicplayer.Constant
import com.example.musicplayer.R
import com.example.musicplayer.databinding.FragmentPlaySongBinding
import com.example.musicplayer.service.MusicService

class PlaySongFragment : Fragment(), View.OnClickListener {

    private lateinit var binding: FragmentPlaySongBinding
    override fun onClick(v: View?) {
        when (v?.id){
            R.id.img_previous -> clickOnPreviousButton()
            R.id.img_next -> clickOnNextButton()
            R.id.img_play -> clickOnPlayButton()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlaySongBinding.inflate(inflater, container, false)
        showSongInfo(requireContext())
        initListener()
        handleMusicAction()
        return binding.root
    }

    private fun showSongInfo(context: Context){
        val currentSong = MusicService.listSongPlaying[MusicService.songPosition]
        binding.tvSongName.text = currentSong.title.toString()
        binding.tvArtist.text = currentSong.artist.toString()
        Glide.with(context).load(currentSong.image.toString()).into(binding.imgSong)
    }

    private fun initListener(){
        binding.imgPlay.setOnClickListener(this)
        binding.imgNext.setOnClickListener(this)
        binding.imgPrevious.setOnClickListener(this)
    }

    private fun handleMusicAction(){
        when (MusicService.songAction){
            Constant.PLAY -> {
                showSongInfo(requireContext())
                showStatusButtonPlay()
            }
            Constant.NEXT -> {showSongInfo(requireContext())}
            Constant.PAUSE -> {showStatusButtonPlay()}
            Constant.RESUME -> {showStatusButtonPlay()}
        }
    }

    private fun clickOnPreviousButton(){
        val musicService = Intent(requireContext(), MusicService::class.java)
        musicService.putExtra(Constant.MUSIC_ACTION, Constant.PREVIOUS)
        musicService.putExtra(Constant.SONG_POSITION, MusicService.songPosition)
        requireContext().startService(musicService)
    }

    private fun clickOnNextButton(){
        val musicService = Intent(requireContext(), MusicService::class.java)
        musicService.putExtra(Constant.MUSIC_ACTION, Constant.NEXT)
        musicService.putExtra(Constant.SONG_POSITION, MusicService.songPosition)
        requireContext().startService(musicService)
    }

    private fun clickOnPlayButton(){
        if (MusicService.mediaPlayer?.isPlaying == true){
            val musicService = Intent(requireContext(), MusicService::class.java)
            musicService.putExtra(Constant.MUSIC_ACTION, Constant.PAUSE)
            musicService.putExtra(Constant.SONG_POSITION, MusicService.songPosition)
            requireContext().startService(musicService)
        }
        else{
            val musicService = Intent(requireContext(), MusicService::class.java)
            musicService.putExtra(Constant.MUSIC_ACTION, Constant.RESUME)
            musicService.putExtra(Constant.SONG_POSITION, MusicService.songPosition)
            requireContext().startService(musicService)
        }
    }

    private fun showStatusButtonPlay(){
        if (MusicService.mediaPlayer?.isPlaying == true) {
            binding.imgPlay.setImageResource(R.drawable.ic_pause_black)
        } else {
            binding.imgPlay.setImageResource(R.drawable.ic_play_black)
        }
    }
}