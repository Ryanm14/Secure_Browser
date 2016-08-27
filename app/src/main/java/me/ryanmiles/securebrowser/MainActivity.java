package me.ryanmiles.securebrowser;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.creativityapps.gmailbackgroundlibrary.BackgroundMail;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import me.ryanmiles.securebrowser.events.OpenWebViewFragment;
import me.ryanmiles.securebrowser.events.SavedStudentInfo;
import me.ryanmiles.securebrowser.fragments.SetupFragment;
import me.ryanmiles.securebrowser.fragments.StudentInfoFragment;
import me.ryanmiles.securebrowser.fragments.WebViewFragment;
import me.ryanmiles.securebrowser.model.TabOut;

import static me.ryanmiles.securebrowser.Data.FIRST_NAME;
import static me.ryanmiles.securebrowser.Data.LAST_NAME;
import static me.ryanmiles.securebrowser.Data.SHAREDPREF_FIRST_NAME_KEY;
import static me.ryanmiles.securebrowser.Data.SHAREDPREF_LAST_NAME_KEY;
import static me.ryanmiles.securebrowser.Data.SHAREDPREF_STUDENT_ID_KEY;
import static me.ryanmiles.securebrowser.Data.STUDENT_ID;

public class MainActivity extends AppCompatActivity {

    public static final int PERMISSION_ALL = 1;
    private static final String TAG = MainActivity.class.getCanonicalName();
    public static String address = "";

    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EventBus.getDefault().register(this);
        String[] PERMISSIONS = {Manifest.permission.CAMERA};
        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }

        if (FIRST_NAME == null || LAST_NAME == null || STUDENT_ID == 0) {
            SwitchStudentInfoFragment();
        } else {
            Log.d(TAG, "Student First Name: [" + FIRST_NAME + "] Student Last Name: [" + LAST_NAME + "] Student ID: [" + STUDENT_ID + "]");
            SetupFragment mSetupFragment = new SetupFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.frame_layout, mSetupFragment, "SetupFragment")
                    .commit();
        }
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

    @Subscribe
    public void onEvent(SavedStudentInfo event) {
        Log.v(TAG, "onEvent() called with: " + "event = [" + event + "]");
        SharedPreferences.Editor prefs = getSharedPreferences("me.ryanmiles.securebrowser", MODE_PRIVATE).edit();
        prefs.putString(SHAREDPREF_FIRST_NAME_KEY, FIRST_NAME);
        prefs.putString(SHAREDPREF_LAST_NAME_KEY, LAST_NAME);
        prefs.putInt(SHAREDPREF_STUDENT_ID_KEY, STUDENT_ID);
        prefs.commit();

        Log.d(TAG, "Student First Name: [" + FIRST_NAME + "] Student Last Name: [" + LAST_NAME + "] Student ID: [" + STUDENT_ID + "]");

        SetupFragment setupFragment = new SetupFragment();
        FragmentManager trans = getSupportFragmentManager();
        trans.beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.frame_layout, setupFragment, "SetupFragment")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.change_info:
                SwitchStudentInfoFragment();
                return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    private void SwitchStudentInfoFragment() {
        StudentInfoFragment mStudentInfoFragment = new StudentInfoFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_layout, mStudentInfoFragment, "StudentInfo")
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (!Data.TABOUTLIST.isEmpty()) {
            String body = "";
            String subject = "";
            for (TabOut tabOut : Data.TABOUTLIST) {
                body += "\n#" + Data.TABOUTLIST.indexOf(tabOut) + " Tab Out = Duration: " + tabOut.getDuration() + " Severity: " + tabOut.getSeverity();
                if (tabOut.getSeverity().equals("High")) {
                    subject = "[High]";
                }
            }
            if (subject.equals("")) {
                subject = "Low / Medium";
            }
            BackgroundMail.newBuilder(this)
                    .withUsername("ryanm934@millcreekhs.com")
                    .withPassword("newman14")
                    .withMailto(address)
                    .withSubject(subject + " Tab out Report for Ryan Miles")
                    .withBody("Tabout out report for Ryan Miles (200224934): \n" + body)
                    .withOnSuccessCallback(new BackgroundMail.OnSuccessCallback() {
                        @Override
                        public void onSuccess() {
                            Toast.makeText(MainActivity.this, "Sent Email!", Toast.LENGTH_LONG).show();
                        }
                    })
                    .withOnFailCallback(new BackgroundMail.OnFailCallback() {
                        @Override
                        public void onFail() {
                            //do some magic
                        }
                    })
                    .send();
        }
        Log.d(TAG,Data.TABOUTLIST.toString());
        Data.TABOUTLIST.clear();
    }

}
