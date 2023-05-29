package com.example.musicplayer

import android.app.Activity
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.os.Build
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import com.example.musicplayer.receiver.MusicReceiver
import com.example.musicplayer.service.MusicService
import java.text.Normalizer
import java.util.regex.Pattern


class GlobalFunction {
    companion object {
        fun startActivity(context: Context, clz: Class<*>?) {
            val intent = Intent(context, clz)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }

        fun startMusicService(ctx: Context, action: Int, songPosition: Int) {
            val musicService = Intent(ctx, MusicService::class.java)
            musicService.putExtra(Constant.MUSIC_ACTION, action)
            musicService.putExtra(Constant.SONG_POSITION, songPosition)
            ContextCompat.startForegroundService(ctx, musicService)
        }

        fun getTextSearch(input: String?): String {
            val nfdNormalizedString = Normalizer.normalize(input, Normalizer.Form.NFD)
            val pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+")
            return pattern.matcher(nfdNormalizedString).replaceAll("")
        }

        fun openMusicReceiver(ctx: Context, action: Int): PendingIntent {
            val intent = Intent(ctx, MusicReceiver::class.java)
            intent.putExtra(Constant.MUSIC_ACTION, action)

            val pendingFlag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            } else {
                PendingIntent.FLAG_UPDATE_CURRENT
            }

            return PendingIntent.getBroadcast(ctx.applicationContext, action, intent, pendingFlag)
        }

        fun getCircularBitmap(bitmap: Bitmap): Bitmap {
            // get the bitmap width and height, and determine the radius of the circle
            val width = bitmap.width
            val height = bitmap.height
            val radius = if (width < height) width / 2 else height / 2

            // create a new bitmap with the same width and height as the original, and a transparent color
            val output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(output)
            canvas.drawColor(Color.TRANSPARENT)

            // create a new paint object to draw the circle
            val paint = Paint()
            paint.isAntiAlias = true
            paint.style = Paint.Style.FILL

            // draw the circle using the paint object
            canvas.drawCircle(width / 2f, height / 2f, radius.toFloat(), paint)

            // use the SRC_IN blend mode to crop the bitmap to the circle
            paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
            canvas.drawBitmap(bitmap, 0f, 0f, paint)

            // trim all space around the circle
            val newWidth = output.width
            val newHeight = output.height
            var left = newWidth
            var top = newHeight
            var right = 0
            var bottom = 0
            for (x in 0 until newWidth) {
                for (y in 0 until newHeight) {
                    if (output.getPixel(x, y) != Color.TRANSPARENT) {
                        if (x < left) left = x
                        if (x > right) right = x
                        if (y < top) top = y
                        if (y > bottom) bottom = y
                    }
                }
            }
            if (right < left || bottom < top) return output // Empty bitmap
            return Bitmap.createBitmap(output, left, top, right - left + 1, bottom - top + 1)
        }

        fun processForShuffle() {
            if (!MusicService.isShuffle) {
                MusicService.currentListSong = ArrayList(MusicService.originalListSong)
                val currentPosition = MusicService.songPosition
                val currentSong = MusicService.currentListSong[currentPosition]
                val shuffledPosition = MusicService.originalListSong.indexOf(currentSong)
                MusicService.songPosition = shuffledPosition
            } else {
                MusicService.currentListSong = ArrayList(MusicService.shuffleListSong)
                val currentPosition = MusicService.songPosition
                val currentSong = MusicService.currentListSong[currentPosition]
                val shuffledPosition = MusicService.shuffleListSong.indexOf(currentSong)
                MusicService.songPosition = shuffledPosition
            }
        }

        fun hideSoftKeyboard(activity: Activity) {
            try {
                val inputMethodManager =
                    activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(activity.currentFocus!!.windowToken, 0)
            } catch (ex: NullPointerException) {
                ex.printStackTrace()
            }
        }
    }
}