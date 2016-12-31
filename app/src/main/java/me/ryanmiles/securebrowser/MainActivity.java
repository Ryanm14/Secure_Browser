package me.ryanmiles.securebrowser;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
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

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import me.ryanmiles.securebrowser.events.BackPress;
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
import static me.ryanmiles.securebrowser.Data.START_TIME;
import static me.ryanmiles.securebrowser.Data.STUDENT_ID;

public class MainActivity extends AppCompatActivity {

    public static final int PERMISSION_ALL = 1;
    private static final String TAG = MainActivity.class.getCanonicalName();

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

    public static String POST() {
        InputStream inputStream = null;
        String result = "";
        String url = "https://gwinnett-county-browser-ryanm14.c9users.io/tests";
        double test_duration = (double) (System.currentTimeMillis() - START_TIME) / 1000;
        try {

            // 1. create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // 2. make POST request to the given URL
            HttpPost httpPost = new HttpPost(url);

            String json = "";

            // 3. build jsonObject
            JSONObject mainJsonObject = new JSONObject();

            JSONObject studentJsonObject = new JSONObject();
            JSONArray arrayForStudent = new JSONArray();
            studentJsonObject.accumulate("teacher_first_name", "Greg");
            studentJsonObject.accumulate("teacher_last_name", "Marr");
            studentJsonObject.accumulate("student_first_name", FIRST_NAME);
            studentJsonObject.accumulate("student_last_name", LAST_NAME);
            studentJsonObject.accumulate("severity", "High");
            studentJsonObject.accumulate("test_duration", test_duration);
            arrayForStudent.put(studentJsonObject);


            JSONArray arrayForLengths = new JSONArray();
            for (TabOut tabOut : Data.TABOUTLIST) {
                JSONObject itemLength = new JSONObject();
                itemLength.put("length", tabOut.getDuration());
                arrayForLengths.put(itemLength);
            }

            mainJsonObject.put("test", arrayForStudent.get(0));
            mainJsonObject.put("tab_outs", arrayForLengths);

            // 4. convert JSONObject to JSON to String
            json = mainJsonObject.toString();
            Log.d("POST[Tests]: ", json);
            // ** Alternative way to convert Person object to JSON string usin Jackson Lib
            // ObjectMapper mapper = new ObjectMapper();
            // json = mapper.writeValueAsString(person);

            // 5. set json to StringEntity
            StringEntity se = new StringEntity(json);

            // 6. set httpPost Entity
            httpPost.setEntity(se);

            // 7. Set some headers to inform server about the type of the content
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            // 8. Execute POST request to the given URL
            HttpResponse httpResponse = httpclient.execute(httpPost);

            // 9. receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // 10. convert inputstream to string
            if (inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        // 11. return result
        return result;
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

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
        START_TIME = System.currentTimeMillis();
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

    @Subscribe
    public void onEvent(BackPress event) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.d(TAG, Data.TABOUTLIST.toString());
        new HttpAsyncTask().execute();

        }

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            return POST();
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getBaseContext(), "Data Sent!", Toast.LENGTH_LONG).show();
            Data.TABOUTLIST.clear();
        }
    }

}
