package elarham.tahfizh.ictaq.MainFragments;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.PermissionRequest;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import elarham.tahfizh.ictaq.MainActivity;
import elarham.tahfizh.ictaq.R;

import static android.content.ContentValues.TAG;

public class VidcallFragment extends Fragment {


    private WebView mWebRTCWebView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vidcall, container, false);

        mWebRTCWebView = view.findViewById(R.id.main_webview);

        setUpWebViewDefaults(mWebRTCWebView);

        mWebRTCWebView.loadUrl("https://appr.tc/r/ictaqrioplewqelkshfjrslk");
        mWebRTCWebView.getSettings().setDomStorageEnabled(true);
        mWebRTCWebView.getSettings().setPluginState(WebSettings.PluginState.ON);
        mWebRTCWebView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onPermissionRequest(final PermissionRequest request) {
                request.grant(request.getResources());
//                Log.d(TAG, "onPermissionRequest");
//                getActivity().runOnUiThread(new Runnable() {
//                    @TargetApi(Build.VERSION_CODES.M)
//                    @Override
//                    public void run() {
//                        if (request.getOrigin().toString().equals("https://appr.tc/r/ictaq121212")) {
//                            request.grant(request.getResources());
//                        } else {
//                            request.deny();
//                        }
//                    }
//                });
            }

        });


        return view;
    }

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

