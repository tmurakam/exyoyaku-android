package org.tmurakam.exyoyaku;

import android.webkit.*;
import android.content.SharedPreferences;

import android.util.Log;

public class ExWebChromeClient extends WebChromeClient {
    private SharedPreferences pref;
	
    @Override
    public void onProgressChanged(WebView wv, int progress) {
        Log.d("ExYoyaku", "onProgressChanged = " + progress + " " + wv.getUrl());
        if (progress != 100) return;

        if (autoLogin(wv)) {
        	return;
        }

        if (wv.getUrl().indexOf("https://shinkansen1.jr-central.co.jp/RSV_P") >= 0) {
            fixPage(wv);
        }
    }

    // never interrupt javascript execution
    @Override
    public boolean onJsTimeout() {
        Log.d("ExYoyaku", "onJsTimeout()");
        return false;
    }

    public void setPref(SharedPreferences p) {
    	pref = p;
    }
    
    public boolean autoLogin(WebView wv) {
        String js, fmt;
        
        if (wv.getUrl().indexOf("http://expy.jp/member/login") < 0) {
        	return false;
        }
        
        String uid = pref.getString("UserId", "");
        String pass = pref.getString("Password", "");

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

    private void fixPage(WebView wv) {
        String js, fmt;

        js = "javascript:"
            + "var f1 = window.frames[0];"
            + "if (f1) { f1.onresize = undefined; var f2 = f1.frames[0];"
            + "if (f2) { f2.onresize = undefined;";
		
        // style を修正する
        fmt = "e = f2.document.getElementById(\"%s\"); if (e) e.setAttribute(\"style\", \"%s\");";

        js += String.format(fmt, "top",     "position:absolute; width:auto; float:left;");
        js += String.format(fmt, "bottom",  "position:absolute; top:50; bottom:auto; width:auto; float:left;");
        js += String.format(fmt, "side",    "position:absolute; top:85; left:0; height:auto; float:left;");
        js += String.format(fmt, "guide",   "top:85; width:auto; float:right;");
        js += String.format(fmt, "content", "width:auto; height:auto; float:right;");

        // A タグの href 属性を書き換える
        js += "e = f2.document.getElementsByTagName(\"a\");";
        js += "for (var i = 0; i < e.length; i++) {";
        js += "  if (e[i].getAttribute(\"href\") == \" \") {";
        js += "    e[i].setAttribute(\"href\", "javascript:void(0)");";
        js += "}}"
		
        js += "}}";
			
        runJs(wv, js);
    }

    private void runJs(WebView wv, String js) {
        wv.loadUrl(js);
        Log.d("ExYoyaku", "exec js: " + js);
    }
}
