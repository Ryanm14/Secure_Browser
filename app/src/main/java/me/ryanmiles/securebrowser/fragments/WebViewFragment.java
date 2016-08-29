package me.ryanmiles.securebrowser.fragments;


import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.ryanmiles.securebrowser.Data;
import me.ryanmiles.securebrowser.ObservableWebView;
import me.ryanmiles.securebrowser.R;
import me.ryanmiles.securebrowser.events.BackPress;
import me.ryanmiles.securebrowser.model.TabOut;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WebViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WebViewFragment extends Fragment {
    private static final String ARG_LINK = "link";
    private static final String TAG = WebViewFragment.class.getCanonicalName();
    @BindView(R.id.webview)
    ObservableWebView mWebView;
    @BindView(R.id.webviewlayout)
    FrameLayout frameLayout;

    Snackbar snackbar;
    private String mLink;
    private long startTabOutTime = 0;


    public WebViewFragment() {
        // Required empty public constructor
    }

    public static WebViewFragment newInstance(String link) {
        WebViewFragment fragment = new WebViewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_LINK, link);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mLink = getArguments().getString(ARG_LINK);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_web_view, container, false);
        ButterKnife.bind(this,rootView);
        mWebView.setWebViewClient(new WebViewClient());
        mWebView.getSettings().setJavaScriptEnabled(true);
        snackbar = Snackbar
                .make(frameLayout, "End Test", Snackbar.LENGTH_INDEFINITE)
                .setAction("End", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        EventBus.getDefault().post(new BackPress());
                    }
                });

        mWebView.setOnScrollChangeListener(new ObservableWebView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(WebView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                Log.d(TAG, "% Scrolled: " + calculateProgression(mWebView));
                snackbar.show();
            }
        });
        mWebView.loadUrl(mLink);
        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called" + "");
        startTabOutTime = System.currentTimeMillis();
    }

    @Override
    public void onResume() {
        super.onResume();
        long endTabOutTime = System.currentTimeMillis();
        if(startTabOutTime != 0){
            Log.d(TAG,"Added new Tab");
            Data.TABOUTLIST.add(new TabOut(startTabOutTime, endTabOutTime));
            startTabOutTime = 0;
        }
    }


    private double calculateProgression(WebView content) {
        float a = content.getScaleY();
        double contentHeight = Math.floor(content.getContentHeight() * content.getScaleY());
        float currentScrollPosition = content.getScrollY();
        double percentWebview = (currentScrollPosition) / contentHeight;
        return percentWebview * 100;
    }
}
