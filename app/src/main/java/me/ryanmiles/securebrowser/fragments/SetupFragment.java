package me.ryanmiles.securebrowser.fragments;

import android.graphics.PointF;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Toast;

import com.dlazaro66.qrcodereaderview.QRCodeReaderView;
import com.dlazaro66.qrcodereaderview.QRCodeReaderView.OnQRCodeReadListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;
import me.ryanmiles.securebrowser.BuildConfig;
import me.ryanmiles.securebrowser.MainActivity;
import me.ryanmiles.securebrowser.R;
import me.ryanmiles.securebrowser.events.OpenWebViewFragment;

/**
 * Created by Ryan Miles on 8/13/2016.
 */
public class SetupFragment extends Fragment implements OnQRCodeReadListener {

    private static final String TAG = SetupFragment.class.getCanonicalName();

    @BindView(R.id.link_edit_text)
    MaterialEditText mLinkEditText;
    @BindView(R.id.email_edit_text)
    MaterialEditText mAddressEditText;
    @BindView(R.id.qrdecoderview)
    QRCodeReaderView qrCodeReaderView;
    @BindView(R.id.checkbox)
    CheckBox checkBox;
    public SetupFragment(){
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.v(TAG, "onCreateView()");
        View rootView =  inflater.inflate(R.layout.fragment_setup, container, false);
        ButterKnife.bind(this,rootView);
        qrCodeReaderView.setOnQRCodeReadListener(this);
        qrCodeReaderView.setAutofocusInterval(2000L);
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkBox.isChecked()) {
                    qrCodeReaderView.setVisibility(View.VISIBLE);
                } else {
                    qrCodeReaderView.setVisibility(View.GONE);
                }
            }
        });
        return rootView;
    }
    @OnClick(R.id.openLink)
    public void openLink(){
        String link = mLinkEditText.getText().toString();
        MainActivity.address = mAddressEditText.getText().toString();
        EventBus.getDefault().post(new OpenWebViewFragment(link));
    }
    @OnLongClick(R.id.openLink)
    public boolean showDebug(){
        Toast.makeText(getActivity(), "Debug - Name: " + BuildConfig.VERSION_NAME + " Number: " + BuildConfig.VERSION_CODE, Toast.LENGTH_LONG).show();
        return true;
    }

    @Override
    public void onQRCodeRead(String text, PointF[] points) {
        mLinkEditText.setText(text);
    }


    @Override
    public void onResume() {
        super.onResume();
        qrCodeReaderView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        qrCodeReaderView.stopCamera();
    }



}
