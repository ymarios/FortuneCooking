package com.adl.fortunecooking

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import com.adl.fortunecooking.model.ResepModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.rtsp.RtspMediaSource
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.BandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import kotlinx.android.synthetic.main.activity_detail_resep.*
import kotlinx.android.synthetic.main.activity_play_video.*

class PlayVideo : AppCompatActivity() {
    lateinit var data:ResepModel
    val url:String = "https://firebasestorage.googleapis.com/v0/b/fortunecooking-1a02f.appspot.com/o/Resep_Cara_Membuat_Nugget_Ayam_Enak_How_to_Make_a_Delicious_Chicken_Nugget.mp4?alt=media&token=bdc5721b-2651-414f-b3b4-119e3a997b02"
    val url2:String= "rtsp://wowzaec2demo.streamlock.net/vod/mp4:BigBuckBunny_115k.mp4"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play_video)
        val myWebView: WebView = findViewById(R.id.webview)
        if(intent.hasExtra("data")) {
            data = intent.getParcelableExtra("data")!!
            myWebView.loadUrl(data.videoUri)
        }



//        var bandwithMeter : BandwidthMeter = DefaultBandwidthMeter()
//
//        var videoTrackSelectionFactory = AdaptiveTrackSelection.Factory()
//
//        var trackSelector = DefaultTrackSelector(videoTrackSelectionFactory)
//
//        var player =  SimpleExoPlayer.Builder(this)
//            .setTrackSelector(trackSelector)
//            .build()
//
//        videoPlayer.setPlayer(player)
//
//        val mediaSource: MediaSource = RtspMediaSource.Factory()
//            .createMediaSource(MediaItem.fromUri(url))
//
//
//
//        player.setMediaSource(mediaSource)
//
//        player.prepare()
    }
}