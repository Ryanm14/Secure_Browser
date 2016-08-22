package me.ryanmiles.securebrowser.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.ryanmiles.securebrowser.Data;
import me.ryanmiles.securebrowser.R;
import me.ryanmiles.securebrowser.model.TabOut;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WebViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WebViewFragment extends Fragment {
    private static final String ARG_LINK = "link";
    private static final String TAG = WebViewFragment.class.getCanonicalName();
    private String mLink;
    private long startTabOutTime = 0;

    @BindView(R.id.webview)
    WebView mWebView;


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
        mWebView.loadUrl(mLink);
        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called with: " + "");
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
}
