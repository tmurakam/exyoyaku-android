package org.tmurakam.exyoyaku;

import android.webkit.*;

public class ExWebViewClient extends WebViewClient {
	@Override
	public void onPageFinished(WebView wv, String url) {
		if (url.indexOf("https://shinkansen1.jr-central.co.jp/RSV_P") < 0) {
			return;
		}
		
		String js = "javascript:"
			+ "var f1 = window.frames[0];"
			+ "if (f1) { f1.onresize = undefined; var f2 = f1.frames[0];"
        	+ "if (f2) { f2.onresize = undefined;";
		
		String fmt = "e = f2.document.getElementById(\"%s\"); if (e) e.setAttribute(\"style\", \"%s\");";

		js += String.format(fmt, "top",     "position:absolute; width:auto; float:left;");
		js += String.format(fmt, "bottom",  "position:absolute; top:50; bottom:auto; width:auto; float:left;");
		js += String.format(fmt, "side",    "position:absolute; top:85; left:0; height:auto; float:left;");
		js += String.format(fmt, "guide",   "top:85; width:auto; float:right;");
		js += String.format(fmt, "content", "width:auto; height:auto; float:right;");
		
		js += "}}";
			
		wv.loadUrl(js);
	}
}
