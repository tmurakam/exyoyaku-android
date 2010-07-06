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
    private WebView webView;
    private ExWebChromeClient webChromeClient;
    private static final int SHOW_CONFIGVIEW = 0;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.main);
        
        SharedPreferences pref = this.getSharedPreferences("userConfig", Activity.MODE_PRIVATE);
        
        webView = new WebView(this);
        //webView.setWebViewClient(new ExWebViewClient());
        webView.setWebViewClient(new WebViewClient());
        
        webChromeClient = new ExWebChromeClient();
        webChromeClient.setPref(pref);
        webView.setWebChromeClient(webChromeClient);
        
        webView.setLayoutParams(new LinearLayout.LayoutParams(
        		LinearLayout.LayoutParams.FILL_PARENT, 
        		LinearLayout.LayoutParams.FILL_PARENT));
        setContentView(webView);

        WebSettings s = webView.getSettings();
        s.setBuiltInZoomControls(true);
        s.setJavaScriptEnabled(true);

        if (savedInstanceState == null) {
        	webView.loadUrl("http://expy.jp/member/login/index.html");
        } else {
        	webView.restoreState(savedInstanceState);
        }
    }
    
    @Override
    protected void onSaveInstanceState(Bundle outState) {
    	super.onSaveInstanceState(outState);
    	webView.saveState(outState);
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
    		startActivityForResult(config, SHOW_CONFIGVIEW);
    		break;
    	}
    	return super.onOptionsItemSelected(item);
    }
    
    @Override
    protected void onActivityResult(int reqCode, int resCode, Intent data) {
    	if (reqCode == SHOW_CONFIGVIEW) {
    		if (resCode == RESULT_OK) {
    			webChromeClient.autoLogin(webView);
    		}
    	}
    }
}