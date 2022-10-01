package imp.mia.siklus;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Custom Web Service Call
        CustomWebViewClient client = new CustomWebViewClient(this);

        //SwipeDown Reload
        swipeRefreshLayout = findViewById(R.id.SwipeDown);

        //WebView
        webView = findViewById(R.id.SiklusWV);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webView.setWebViewClient(client);
        webView.loadUrl(URL);

        //Swipe Listener
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                webView.reload();
            }
        });
    }
    //Custom Client
    class CustomWebViewClient extends WebViewClient
    {
        private Activity activity;
        CustomWebViewClient(Activity activity)
        {
            this.activity = activity;
        }

        //For Reload
        @Override
        public void onPageFinished(WebView view, String url)
        {
            super.onPageFinished(view, url);
            swipeRefreshLayout.setRefreshing(false);
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
        //super.onBackPressed();
        //For exit alert
        else
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Apakah kamu sudah berlatih hari ini? \n Yakin ingin keluar?")
                    .setNegativeButton("Tidak", null)
                    .setPositiveButton("Iya", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finishAffinity();
                        }
                    }).show();
        }
    }
}