package me.ryanmiles.securebrowser.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.rengwuxian.materialedittext.MaterialEditText;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;
import me.ryanmiles.securebrowser.BuildConfig;
import me.ryanmiles.securebrowser.R;
import me.ryanmiles.securebrowser.events.OpenWebViewFragment;

/**
 * Created by Ryan Miles on 8/13/2016.
 */
public class SetupFragment extends Fragment {

    private static final String TAG = SetupFragment.class.getCanonicalName();
    MaterialEditText mMaterialEditText;

    public SetupFragment(){
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.v(TAG, "onCreateView()");
        View rootView =  inflater.inflate(R.layout.fragment_setup, container, false);
        mMaterialEditText = (MaterialEditText) rootView.findViewById(R.id.link_edit_text);
        ButterKnife.bind(this,rootView);
        return rootView;
    }
    @OnClick(R.id.openLink)
    public void openLink(){
        String link = mMaterialEditText.getText().toString();
        EventBus.getDefault().post(new OpenWebViewFragment(link));
    }
    @OnLongClick(R.id.openLink)
    public boolean showDebug(){
        Toast.makeText(getActivity(), "Debug - Name: " + BuildConfig.VERSION_NAME + " Number: " + BuildConfig.VERSION_CODE, Toast.LENGTH_LONG).show();
        return true;
    }

}
