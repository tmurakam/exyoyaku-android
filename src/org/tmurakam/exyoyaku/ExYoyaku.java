
package org.tmurakam.exyoyaku;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.webkit.*;
import android.content.Intent;
import android.view.MenuInflater;
import android.view.Window;
import android.view.Menu;
import android.view.MenuItem;

public class ExYoyaku extends Activity {
    private final static String HOME_URL = "http://expy.jp/member/login/index.html";

    private WebView webView;

    private ExWebChromeClient webChromeClient;

    private static final int SHOW_CONFIGVIEW = 0;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // use progress bar
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

        // WebView 生成
        webView = new WebView(this);

        // ExWebViewClient を生成
        webView.setWebViewClient(new ExWebViewClient(this));

        // ExWebChromeClent を生成
        webChromeClient = new ExWebChromeClient(this, webView);
        webView.setWebChromeClient(webChromeClient);

        // WebView レイアウトを設定
        webView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));
        setContentView(webView);

        // WebView 設定を変更
        WebSettings s = webView.getSettings();
        s.setBuiltInZoomControls(true);
        s.setJavaScriptEnabled(true);

        // ページロード
        if (savedInstanceState == null) {
            goHome();
        } else {
            webView.restoreState(savedInstanceState);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        webView.saveState(outState);
    }

    private void goHome() {
        webView.loadUrl(HOME_URL);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        new MenuInflater(this).inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_home:
                goHome();
                return true;

            case R.id.menu_config:
                Intent config = new Intent(this, PrefActivity.class);
                startActivityForResult(config, SHOW_CONFIGVIEW);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int reqCode, int resCode, Intent data) {
        if (reqCode == SHOW_CONFIGVIEW) {
            webChromeClient.autoLogin(webView);
        }
    }

    // for rotation change
    @Override
    public void onConfigurationChanged(android.content.res.Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    // page error
    public void onWebViewError(String url) {
        // webView.goBack(); // goBack は駄目。ログイン画面に戻ってしまう。
        // webView.reload(); // これも駄目
    }
}
