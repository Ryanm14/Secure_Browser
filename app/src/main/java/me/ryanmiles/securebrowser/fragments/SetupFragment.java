package me.ryanmiles.securebrowser.fragments;

import android.graphics.PointF;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.Toast;

import com.dlazaro66.qrcodereaderview.QRCodeReaderView;
import com.dlazaro66.qrcodereaderview.QRCodeReaderView.OnQRCodeReadListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;
import me.ryanmiles.securebrowser.BuildConfig;
import me.ryanmiles.securebrowser.Data;
import me.ryanmiles.securebrowser.R;
import me.ryanmiles.securebrowser.events.OpenWebViewFragment;

import static me.ryanmiles.securebrowser.Data.EMAIL_ADDRESS;
import static me.ryanmiles.securebrowser.Data.EMAIL_LIST;

/**
 * Created by Ryan Miles on 8/13/2016.
 */
public class SetupFragment extends Fragment implements OnQRCodeReadListener {

    private static final String TAG = SetupFragment.class.getCanonicalName();

    @BindView(R.id.link_edit_text)
    MaterialEditText mLinkEditText;
    @BindView(R.id.spinner_setup_fragment)
    Spinner mAddressSpinner;
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
        List<String> address_name = new ArrayList<>(EMAIL_LIST.keySet());
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, address_name);
        mAddressSpinner.setAdapter(adapter);
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
        int spinner_postion = mAddressSpinner.getSelectedItemPosition();
        List<String> indexes = new ArrayList<>(EMAIL_LIST.values());
        String address = indexes.get(spinner_postion);
        if (link.equals("") || address.equals("")) {
            Toast.makeText(getActivity(), "Make sure the fields are not blank!", Toast.LENGTH_LONG).show();
        } else {
            EMAIL_ADDRESS = address;
            if (!link.startsWith("http://") && !link.startsWith("https://")) {
                link = "http://" + link;
            }
            Data.START_TIME = System.currentTimeMillis();
            Data.temp = true;
            EventBus.getDefault().post(new OpenWebViewFragment(link));
        }
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
