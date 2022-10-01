package imp.mia.siklus;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends AppCompatActivity
{
    private WebView webView;
    private String URL = "https://siklushealthy.com/";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CustomWebViewClient client = new CustomWebViewClient(this);

        //WebView
        webView = findViewById(R.id.SiklusWV);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webView.setWebViewClient(client);
        webView.loadUrl(URL);
    }
    //Custom Client
    class CustomWebViewClient extends WebViewClient
    {
        private Activity activity;
        CustomWebViewClient(Activity activity)
        {
            this.activity = activity;
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.contains(URL))
            {
                view.loadUrl(url);
            }
            else
            {
                Intent i = new Intent(Intent.ACTION_VIEW , Uri.parse(url));
                startActivity(i);
            }
            return true;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK && this.webView.canGoBack())
        {
            this.webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed()
    {
        if (webView.canGoBack())
        {
            webView.goBack();
        }
        super.onBackPressed();
    }
}