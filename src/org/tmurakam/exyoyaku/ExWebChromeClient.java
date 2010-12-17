
package org.tmurakam.exyoyaku;

import android.webkit.*;
import android.content.Context;
import android.content.SharedPreferences;
import android.app.Dialog;
import android.view.View;
import android.util.Log;

/**
 * @brief カスタム WebChromeClient 本アプリの心臓部。オートログイン、画面修正などを行う。
 */
public class ExWebChromeClient extends WebChromeClient {
    private final static String TAG = "ExYoyaku";

    private final static String LOGIN_URL = "http://expy.jp/member/login";

    private final static String INNER_URL_PREFIX = "https://shinkansen1.jr-central.co.jp/RSV_P";

    private View view;

    private Dialog dlg;

    private Context context;

    public ExWebChromeClient(Context context, View view) {
        this.context = context;
        this.view = view;
        dlg = null;
    }

    /**
     * @brief ページ読み込み進行ハンドラ 100% に達したらページ修正、オートログインなどを実施する
     */
    @Override
    public void onProgressChanged(WebView wv, int progress) {
        Log.d(TAG, "onProgressChanged = " + progress + " " + wv.getUrl());
        if (progress < 100) {
            if (dlg == null) {
                dlg = new Dialog(view.getContext());
                dlg.setTitle("Loading...");
                dlg.show();
            }
        } else {
            if (dlg != null) {
                dlg.dismiss();
                dlg = null;
            }

            if (autoLogin(wv)) {
                return;
            }

            String url = wv.getUrl();
            if (url != null && url.startsWith(INNER_URL_PREFIX)) {
                fixPage(wv);
            }
        }
    }

    /**
     * @brief JavaScript タイムアウトハンドラ
     * @note タイムアウトは一切発生させない
     */
    /*
     * API level 7 (android 2.1) later...
     * @Override public boolean onJsTimeout() { Log.d("ExYoyaku",
     * "onJsTimeout()"); return false; }
     */

    /**
     * @brief オートログイン
     */
    public boolean autoLogin(WebView wv) {
        String js, fmt;

        // ログインページに居るかどうか確認
        String url = wv.getUrl();
        if (url == null || !url.startsWith(LOGIN_URL)) {
            return false;
        }

        // User ID / Pass を取得
        SharedPreferences prefs = context.getSharedPreferences(PrefActivity.PREF_NAME,
                Context.MODE_PRIVATE);
        String uid = prefs.getString(PrefActivity.PREF_KEY_USERID, "");
        String pass = prefs.getString(PrefActivity.PREF_KEY_PASSWORD, "");

        // auto login
        for (int i = 1; i <= 2; i++) {
            fmt = "javascript:document.getElementById(\"user_id%d\").value=\"%s\"";
            js = String.format(fmt, i, uid);
            runJs(wv, js);

            fmt = "javascript:document.getElementById(\"password%d\").value=\"%s\"";
            js = String.format(fmt, i, pass);
            runJs(wv, js);
        }

        return true;
    }

    /**
     * @brief ページ補正を行う
     */
    private void fixPage(WebView wv) {
        String js, fmt;

        js = "javascript:" + "var f1 = window.frames[0];"
                + "if (f1) { f1.onresize = undefined; var f2 = f1.frames[0];"
                + "if (f2) { f2.onresize = undefined;";

        // style を修正する
        fmt = "e = f2.document.getElementById(\"%s\"); if (e) e.setAttribute(\"style\", \"%s\");";

        js += String.format(fmt, "top", "position:absolute; width:auto; float:left;");
        js += String.format(fmt, "bottom",
                "position:absolute; top:50; bottom:auto; width:auto; float:left;");
        js += String.format(fmt, "side",
                "position:absolute; top:85; left:0; height:auto; float:left;");
        js += String.format(fmt, "guide", "top:85; width:auto; float:right;");
        js += String.format(fmt, "content", "width:auto; height:auto; float:right;");

        // A タグの href 属性を書き換える (これはあまり関係なしのようだ)
        // js += "e = f2.document.getElementsByTagName(\"a\");";
        // js += "for (var i = 0; i < e.length; i++) {";
        // js += "  if (e[i].getAttribute(\"href\") == \" \") {";
        // js += "    e[i].setAttribute(\"href\", \"javascript:void(0)\");";
        // js += "}}";

        js += "}}";

        runJs(wv, js);
    }

    /**
     * @brief JavaScript 実行
     */
    private void runJs(WebView wv, String js) {
        wv.loadUrl(js);
        Log.d(TAG, "exec js: " + js);
    }

    // debug
    /*
     * @Override public boolean onJsTimeout() { Log.d("ExYoyaku",
     * "onJsTimeout"); return false; }
     */
}
