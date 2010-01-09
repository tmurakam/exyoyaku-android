package org.tmurakam.exyoyaku;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.webkit.*;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.content.SharedPreferences;

public class ExYoyaku extends Activity {
    WebView webView;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.main);
        
        SharedPreferences pref = this.getSharedPreferences("userConfig", Activity.MODE_PRIVATE);
        
        webView = new WebView(this);
        //webView.setWebViewClient(new ExWebViewClient());
        webView.setWebViewClient(new WebViewClient());
        
        ExWebChromeClient wcc = new ExWebChromeClient();
        wcc.setPref(pref);
        webView.setWebChromeClient(wcc);
        
        webView.setLayoutParams(new LinearLayout.LayoutParams(
        		LinearLayout.LayoutParams.FILL_PARENT, 
        		LinearLayout.LayoutParams.FILL_PARENT));
        setContentView(webView);

        WebSettings s = webView.getSettings();
        s.setBuiltInZoomControls(true);
        s.setJavaScriptEnabled(true);

        webView.loadUrl("http://expy.jp/member/login/index.html");
        
        // test
        //Intent test = new Intent(this, ConfigView.class);
        //startActivity(test);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	super.onCreateOptionsMenu(menu);
    	MenuItem item = menu.add(0, 0, Menu.NONE, "Config");
    	item.setIcon(android.R.drawable.ic_menu_preferences);
    	return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
    	case 0:
    		Intent config = new Intent(this, ConfigView.class);
    		startActivity(config);
    		break;
    	}
    	return super.onOptionsItemSelected(item);
    }
    
}