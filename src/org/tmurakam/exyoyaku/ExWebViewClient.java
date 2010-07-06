package org.tmurakam.exyoyaku;

import android.webkit.*;
import android.content.SharedPreferences;
import android.app.Activity;
import android.app.Dialog;
import android.graphics.Bitmap;

import android.util.Log;

/**
   @brief カスタム WebViewClient

   ロード中に progress bar を出すとともに、
   ダイアログを出して操作禁止にする
*/
public class ExWebViewClient extends WebViewClient {
    private Dialog dlg;
    private Activity activity;

    public ExWebViewClient(Activity act) {
        super();
        activity = act;
        dlg = null;
    }

    @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        activity.setProgressBarIndeterminateVisibility(true);

        if (dlg != null) {
            dlg.dismiss();
        }
        dlg = new Dialog(view.getContext());
        dlg.setTitle("Loading...");
        dlg.show();
    }

    @Override
        public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        activity.setProgressBarIndeterminateVisibility(false);
        dlg.dismiss();
        dlg = null;
    }
}
