package elarham.tahfizh.ictaq.MainFragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.webkit.WebView;

import android.widget.VideoView;

import elarham.tahfizh.ictaq.R;
import elarham.tahfizh.ictaq.Global.WebClient;

public class HomeFragment extends Fragment {

    VideoView videoView1, videoView2, videoView3;
    String videoUrl = "http://elarham-tahfizh.online/assets/video/Keutamaan_Penghafal_al-Qur'an.mp4";



    WebView webView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

//        videoView1 =  view.findViewById(R.id.videoView1);
//
//            Uri uri=Uri.parse(videoUrl);
//            videoView1.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//                @Override
//                public void onCompletion(MediaPlayer mp) {
//
//                        videoView1.start();
//
//                }
//            });
//        videoView1.setVideoURI(uri);
//        videoView1.start();

        // Set the media controller buttons

        webView = view.findViewById(R.id.webview);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);


        // Baris di bawah untuk menambahkan scrollbar di dalam WebView-nya
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.setWebViewClient(new WebClient());
        webView.loadUrl("http://elarham-tahfizh.online/index.php/welcome.html#gtco-features");


        return view;
    }
}
