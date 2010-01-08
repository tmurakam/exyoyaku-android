package org.tmurakam.exyoyaku;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.webkit.*;

public class ExYoyaku extends Activity {
    WebView webView;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.main);
        
        webView = new WebView(this);
        //webView.setWebViewClient(new ExWebViewClient());
        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new ExWebChromeClient());
        webView.setLayoutParams(new LinearLayout.LayoutParams(
        		LinearLayout.LayoutParams.FILL_PARENT, 
        		LinearLayout.LayoutParams.FILL_PARENT));
        setContentView(webView);

        WebSettings s = webView.getSettings();
        s.setBuiltInZoomControls(true);
        s.setJavaScriptEnabled(true);

        webView.loadUrl("http://expy.jp/member/login/index.html");
    }
}