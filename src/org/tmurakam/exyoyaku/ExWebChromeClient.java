package org.tmurakam.exyoyaku;

import android.webkit.*;
import android.content.SharedPreferences;

import android.util.Log;public class ExWebChromeClient extends WebChromeClient {
	private SharedPreferences pref;
	
    @Override
    public void onProgressChanged(WebView wv, int progress) {
        Log.d("ExYoyaku", "onProgressChanged = " + progress + " " + wv.getUrl());
        if (progress != 100) return;

        String url = wv.getUrl();
        if (url.indexOf("http://expy.jp/member/login/") >= 0) {
            autoLogin(wv);
        }
        else if (url.indexOf("https://shinkansen1.jr-central.co.jp/RSV_P") >= 0) {
            fixPage(wv);
        }
    }

    public void setPref(SharedPreferences p) {
    	pref = p;
    }
    
    private void autoLogin(WebView wv) {
        String js, fmt;
        
        String uid = pref.getString("UserId", "");
        String pass = pref.getString("Password", "");

        // auto login
        fmt = "javascript:document.getElementById(\"user_id%d\").value=\"%s\"";
        js = String.format(fmt, 1, uid);
        runJs(wv, js);

        fmt = "javascript:document.getElementById(\"password%d\").value=\"%s\"";
        js = String.format(fmt, 1, pass);
        runJs(wv, js);
    }

    private void fixPage(WebView wv) {
        String js, fmt;

        js = "javascript:"
            + "var f1 = window.frames[0];"
            + "if (f1) { f1.onresize = undefined; var f2 = f1.frames[0];"
            + "if (f2) { f2.onresize = undefined;";
		
        fmt = "e = f2.document.getElementById(\"%s\"); if (e) e.setAttribute(\"style\", \"%s\");";

        js += String.format(fmt, "top",     "position:absolute; width:auto; float:left;");
        js += String.format(fmt, "bottom",  "position:absolute; top:50; bottom:auto; width:auto; float:left;");
        js += String.format(fmt, "side",    "position:absolute; top:85; left:0; height:auto; float:left;");
        js += String.format(fmt, "guide",   "top:85; width:auto; float:right;");
        js += String.format(fmt, "content", "width:auto; height:auto; float:right;");
		
        js += "}}";
			
        runJs(wv, js);
    }

    private void runJs(WebView wv, String js) {
        wv.loadUrl(js);
        Log.d("ExYoyaku", "exec js: " + js);
    }
}