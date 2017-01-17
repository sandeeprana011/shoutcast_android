package com.zilideus.jukebox.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import com.zilideus.jukebox.BuildConfig;
import com.zilideus.jukebox.R;

import java.util.Locale;

public class AboutUs extends Fragment {
    Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.about, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        int versionCode = BuildConfig.VERSION_CODE;
        String versionName = BuildConfig.VERSION_NAME;

        TextView textView = (TextView) view.findViewById(R.id.version_no);
        String versionAndBuild = String.format(Locale.getDefault(), "Version Code : %d\n%s", versionCode, versionName);
        textView.setText(versionAndBuild);

        WebView webView = (WebView) view.findViewById(R.id.webview);
        webView.loadUrl("file:///android_asset/license.html");
    }
}