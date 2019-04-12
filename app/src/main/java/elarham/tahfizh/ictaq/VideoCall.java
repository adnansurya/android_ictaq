package elarham.tahfizh.ictaq;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.webkit.CookieManager;
import android.webkit.PermissionRequest;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import elarham.tahfizh.ictaq.Global.SharedPreferenceManager;


public class VideoCall extends AppCompatActivity {

    SharedPreferenceManager sharePrefMan;

    private WebView mWebRTCWebView;
    LinearLayout editLay;
    ActionBar actBar;
    String roomId, url;

    String jadwalId, reqId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_call);

        sharePrefMan = new SharedPreferenceManager(this);
        mWebRTCWebView = findViewById(R.id.main_webview);
        editLay = findViewById(R.id.editLay);




        roomId = getIntent().getStringExtra("idRoom");
        jadwalId = getIntent().getStringExtra("idJadwal");
        reqId = getIntent().getStringExtra("idReq");

        Log.e("ROOM ID", roomId);

        actBar = getSupportActionBar();
        actBar.setTitle(getApplicationContext().getString(R.string.vidcall));
        actBar.setDisplayHomeAsUpEnabled(true);

        setUpWebViewDefaults(mWebRTCWebView);

        url = "https://appr.tc/r/" + roomId+ "?stereo=false&backasc=ISAC/16000&hd=false";

        if(sharePrefMan.getSpType().equals("2")){
            mWebRTCWebView.setVisibility(View.GONE);
            editLay.setVisibility(View.VISIBLE);
        }else if(sharePrefMan.getSpType().equals("3")){
            editLay.setVisibility(View.GONE);
            openRTC();
        }



    }
    private void setWebChromeClient(){

        final ProgressDialog progressDialog = new ProgressDialog(VideoCall.this);
        progressDialog.setMessage(getApplicationContext().getString(R.string.loading));
        progressDialog.show();

        mWebRTCWebView.setWebChromeClient(new WebChromeClient() {


            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
//                Log.e("PROGRESS", String.valueOf(newProgress));
                if(newProgress == 100){
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onPermissionRequest(final PermissionRequest request) {

                request.grant(request.getResources());

            }



        });
    }





    @Override
    public void onStop() {
        super.onStop();

        /**
         * When the application falls into the background we want to stop the media stream
         * such that the camera is free to use by other apps.
         */
        Log.e("VIDCALL", "STOP");

        mWebRTCWebView.loadUrl("about:blank");
    }

    @Override
    protected void onPause() {
        super.onPause();
        mWebRTCWebView.loadUrl("about:blank");
        Log.e("VIDCALL", "PAUSE");
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setUpWebViewDefaults(WebView webView) {
        WebSettings settings = webView.getSettings();



        // Enable Javascript
        settings.setJavaScriptEnabled(true);

        // Use WideViewport and Zoom out if there is no viewport defined
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);

        // Enable pinch to zoom without the zoom buttons
        settings.setBuiltInZoomControls(true);

        // Allow use of Local Storage
        settings.setDomStorageEnabled(true);

        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
            // Hide the zoom controls for HONEYCOMB+
            settings.setDisplayZoomControls(false);
        }

        // Enable remote debugging via chrome://inspect
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }

        webView.setWebViewClient(new WebViewClient());

        // AppRTC requires third party cookies to work
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptThirdPartyCookies(mWebRTCWebView, true);
    }

     @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.videocall_menu, menu);

         if(sharePrefMan.getSpType().trim().equals("3")){
             MenuItem check = menu.findItem(R.id.edit_menu);
             check.setVisible(false);
         }

        return true;
    }

    public void openRTC(){
        mWebRTCWebView.setVisibility(View.VISIBLE);
        mWebRTCWebView.loadUrl(url);
        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECORD_AUDIO)
                == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_GRANTED){

            setWebChromeClient();

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()){
            case android.R.id.home:
                mWebRTCWebView.loadUrl("about:blank");
                editLay.setVisibility(View.GONE);
                onBackPressed();
                return true;
            case R.id.browser_menu:
                mWebRTCWebView.loadUrl("about:blank");
                mWebRTCWebView.setVisibility(View.GONE);
                editLay.setVisibility(View.GONE);
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                break;

            case R.id.exam_menu:
                editLay.setVisibility(View.GONE);
                openRTC();
                break;
            case R.id.edit_menu:
                mWebRTCWebView.loadUrl("about:blank");
                mWebRTCWebView.setVisibility(View.GONE);
                editLay.setVisibility(View.VISIBLE);

                break;
        }


        return super.onOptionsItemSelected(item);
    }
}
