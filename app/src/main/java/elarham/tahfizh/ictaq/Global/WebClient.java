package elarham.tahfizh.ictaq.Global;

import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebClient extends WebViewClient {
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        view.loadUrl(url);
        return true;
    }


    @Override
    public void onPageFinished(WebView view, String url)
    {
        // Obvious next step is: document.forms[0].submit()
        view.loadUrl("javascript:(function() { " +
                "var gotop = document.getElementsByClassName('js-gotop')[0].style.display='none'; " +

                "var header = document.getElementById('gtco-header').style.display='none'; " +
                "var footer = document.getElementById('gtco-footer').style.display='none'; " +


                "})()");
    }

}