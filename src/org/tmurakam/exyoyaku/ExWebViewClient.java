package org.tmurakam.exyoyaku;

import android.webkit.*;
import android.content.SharedPreferences;

import android.util.Log;

public class ExWebViewClient extends WebViewClient {
    private Dialog dlg;

    public ExWebViewClient() {
        super();
        dlg = null;
    }

    @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        setProgressBarIndeterminateVisibility(true);
        dlg = new Dialog(view.getContext());
        dlg.setTitle("Loading...");
        dlg.show();
    }

    @Override
        public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        setProgressBarIndeterminateVisibility(false);
        dlg.dismiss();
        dlg = null;
    }
}
