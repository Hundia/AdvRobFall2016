package com.bluetooth.activities;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.MediaController;
import android.widget.VideoView;

import com.bluetooth.BluetoothActivity;
import com.bluetooth.R;
import com.bluetooth.activities.mjpeg.MjpegInputStream;
import com.bluetooth.activities.mjpeg.MjpegView;

import java.util.HashMap;
import java.util.Map;

public class VidActivity extends BluetoothActivity {

    //final static String RTSP_URL = "rtsp://192.168.1.13:4747/";
    //final static String RTSP_URL = "http://192.168.1.15:4747/mjpegfeed";

    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        //sample public cam
        setContentView(R.layout.activity_vid);
        WebView myWebView = (WebView) findViewById(R.id.webview);
        MediaController vidControl = new MediaController(this);
        vidControl.setAnchorView(myWebView);
//        myWebView.setMediaController(vidControl);

        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.loadUrl("http://192.168.1.9:5000");
//
//        myWebView.setVideoURI("http://192.168.1.9:5000");
//
////        myWebView.setVideoPath("rtsp://192.168.1.9:8554/cam1");
//        myWebView.setVideoPath("http://192.168.1.9:5000");
//        myWebView.start();
    }
}
