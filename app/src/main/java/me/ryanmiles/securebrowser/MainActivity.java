package me.ryanmiles.securebrowser;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import me.ryanmiles.securebrowser.events.OpenWebViewFragment;
import me.ryanmiles.securebrowser.fragments.SetupFragment;
import me.ryanmiles.securebrowser.fragments.WebViewFragment;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getCanonicalName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EventBus.getDefault().register(this);
        SetupFragment mSetupFragment = new SetupFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.frame_layout, mSetupFragment, "SetupFragment")
                .commit();
    }

    @Subscribe
    public void onEvent(OpenWebViewFragment event) {
        Log.v(TAG, "onEvent() called with: " + "event = [" + event + "]");
        WebViewFragment webViewFragment = WebViewFragment.newInstance(event.getLink());
        FragmentManager trans = getSupportFragmentManager();
        trans.beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.frame_layout, webViewFragment, "WebView")
                .addToBackStack(null)
                .commit();
    }
}
