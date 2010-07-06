package org.tmurakam.exyoyaku;

import android.webkit.*;
import android.content.SharedPreferences;

import android.util.Log;

/**
   @brief カスタム WebViewClient

   ロード中に progress bar を出すとともに、
   ダイアログを出して操作禁止にする
*/
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

        if (dlg) {
            dig.dismiss();
        }
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
