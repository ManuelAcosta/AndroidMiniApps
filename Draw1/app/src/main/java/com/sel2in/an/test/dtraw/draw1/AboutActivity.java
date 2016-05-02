package com.sel2in.an.test.dtraw.draw1;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity {
    //only 1 of AboutActivity should be loaded at a time
    Activity activity ;



    public void goweb(View squareClickUi) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://sites.google.com/site/kid4years/"));
        startActivity(browserIntent);
    }


    public void init(){
        String s = getString(R.string.txtAbout2) + "\n" + C3Activity.getStatsFormatted()
                + "\n" + C3Activity.getStatsAllFormatted();
        ((TextView) findViewById(R.id.txt)).setText(s);
    }

    @Override
    protected void onStart(){
        super.onStart();
        init();
    }

    @Override
    protected void onRestart(){
        super.onRestart();
        init();
    }
    @Override
    protected void onResume(){
        super.onResume();
        init();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_about);
        TextView t = (TextView) findViewById(R.id.url1);
        t.setPaintFlags(t.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        try {

            activity = this;

            WebView webview1 = (WebView) findViewById(R.id.webView);
            webview1.getSettings().setJavaScriptEnabled(true);
            webview1.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
            webview1.clearCache(true);
            webview1.getSettings().setBuiltInZoomControls(true);
            //webview1.setInitialScale(50);
            webview1.getSettings().setUseWideViewPort(true);
            webview1.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return true;
                }

                @Override
                public void onPageFinished(WebView view, final String url) {
                    Log.i("aboutWebPage", "Downloaded");
                }
            });
            //String s = Utl.getSmallHtml("https://sites.google.com/site/kid4years/tictactoe");
            webview1.loadUrl("https://sites.google.com/site/kid4years/tictactoe/");//http://sel2in.com/pages/prog/;http://sel2in.com/index.php
        } catch (Exception e) {
            Log.i("aboutCreate", "Err on create " + e, e);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_about, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //startActivity(intentMainGame);
        super.onBackPressed();
        return true;

        //return super.onOptionsItemSelected(item);
    }
}
