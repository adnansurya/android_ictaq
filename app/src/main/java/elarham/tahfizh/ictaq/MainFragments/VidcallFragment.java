package elarham.tahfizh.ictaq.MainFragments;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.CookieManager;
import android.webkit.PermissionRequest;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import elarham.tahfizh.ictaq.Daftar;
import elarham.tahfizh.ictaq.MainActivity;
import elarham.tahfizh.ictaq.R;

import static android.content.ContentValues.TAG;

public class VidcallFragment extends Fragment {


    private WebView mWebRTCWebView;

    private static final int MY_CAMERA_REQUEST_CODE = 100;
    private static final int MY_REC_AUDIO_REQUEST_CODE = 101;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vidcall, container, false);

        mWebRTCWebView = view.findViewById(R.id.main_webview);

        setUpWebViewDefaults(mWebRTCWebView);

        mWebRTCWebView.loadUrl("https://appr.tc/r/ictaqrioplewqelkshfjrslko");
        mWebRTCWebView.getSettings().setDomStorageEnabled(true);


        checkRequestPermission();


        return view;
    }

    private void setWebChromeClient(){

        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage(getContext().getString(R.string.loading));
        progressDialog.show();

        mWebRTCWebView.setWebChromeClient(new WebChromeClient() {


            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                //super.onProgressChanged(view, newProgress);
                Log.e("PROGRESS", String.valueOf(newProgress));
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

    private void checkRequestPermission(){
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);

        }

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.RECORD_AUDIO)
                == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.RECORD_AUDIO}, MY_REC_AUDIO_REQUEST_CODE);

        }

        if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.RECORD_AUDIO)
                == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED){

            setWebChromeClient();

        }

    }


    @Override

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == MY_CAMERA_REQUEST_CODE) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Toast.makeText(getActivity(), getActivity().getString(R.string.cameraallowed), Toast.LENGTH_LONG).show();

            } else {

                Toast.makeText(getActivity(), getActivity().getString(R.string.cameranotallowed), Toast.LENGTH_LONG).show();

            }

        }else if(requestCode == MY_REC_AUDIO_REQUEST_CODE){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Toast.makeText(getActivity(), getActivity().getString(R.string.micallowed), Toast.LENGTH_LONG).show();

            } else {

                Toast.makeText(getActivity(), getActivity().getString(R.string.micnotallowed), Toast.LENGTH_LONG).show();

            }
        }
    }//end onRequestPermissionsResult



    @Override
    public void onStop() {
        super.onStop();

        /**
         * When the application falls into the background we want to stop the media stream
         * such that the camera is free to use by other apps.
         */
        Log.e("FRAGMENT", "TERTUTUP");
        //mWebRTCWebView.evaluateJavascript("location.reload();window.stop();}", null);
        mWebRTCWebView.loadUrl("about:blank");
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


}

