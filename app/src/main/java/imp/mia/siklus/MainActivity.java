package imp.mia.siklus;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity
{
    private WebView webView;
    private String URL = "https://siklushealthy.com/";
    private SwipeRefreshLayout swipeRefreshLayout;
    private RelativeLayout relativeLayout;
    private Button reloadBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Custom Web Service Call
        CustomWebViewClient client = new CustomWebViewClient(this);

        //SwipeDown Reload
        swipeRefreshLayout = findViewById(R.id.SwipeDown);

        //Custom No Internet Page
        relativeLayout = findViewById(R.id.CustomError);

        //Reload Button on Internet Error
        reloadBtn =  findViewById(R.id.ButtonRefresh);

        //WebView
        webView = findViewById(R.id.SiklusWV);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webView.setWebViewClient(client);

        InternetCheck();

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

        reloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(getIntent());
            }
        });
    }

    private void InternetCheck()
    {
        ConnectivityManager connectivityManager =(ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mobileData = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (mobileData.isConnected())
        {
            swipeRefreshLayout.setVisibility(View.VISIBLE);
            webView.setVisibility(View.VISIBLE);
            relativeLayout.setVisibility(View.GONE);
        }
        else if (wifi.isConnected())
        {
            swipeRefreshLayout.setVisibility(View.VISIBLE);
            webView.setVisibility(View.VISIBLE);
            relativeLayout.setVisibility(View.GONE);
        }
        else
        {
            swipeRefreshLayout.setVisibility(View.GONE);
            webView.setVisibility(View.GONE);
            relativeLayout.setVisibility(View.VISIBLE);
        }
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

        //For Button Refresh
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon)
        {
            super.onPageStarted(view, url, favicon);
            InternetCheck();
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url)
        {
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
                    .setPositiveButton("Iya", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            finishAffinity();
                        }
                    }).show();
        }
    }
}