package me.ryanmiles.securebrowser.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.rengwuxian.materialedittext.MaterialEditText;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.ryanmiles.securebrowser.Data;
import me.ryanmiles.securebrowser.R;
import me.ryanmiles.securebrowser.events.SavedStudentInfo;


public class StudentInfoFragment extends Fragment {
    @BindView(R.id.student_first_name)
    MaterialEditText mStudentFirstNameEditText;
    @BindView(R.id.student_last_name)
    MaterialEditText mStudentLastNameEditText;
    @BindView(R.id.student_id)
    MaterialEditText mStudentIdEditText;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_student_info, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @OnClick(R.id.saveInfo)
    public void SaveInfo() {
        if (isEmpty(mStudentFirstNameEditText) || isEmpty(mStudentLastNameEditText) || isEmpty(mStudentIdEditText)) {
            Toast.makeText(getActivity(), "Check your Information!", Toast.LENGTH_LONG).show();
        } else {
            Data.FIRST_NAME = capitalize(mStudentFirstNameEditText.getText().toString().trim());
            Data.LAST_NAME = capitalize(mStudentLastNameEditText.getText().toString().trim());
            Data.STUDENT_ID = Integer.parseInt(mStudentIdEditText.getText().toString().trim());

            EventBus.getDefault().post(new SavedStudentInfo());
        }
    }

    private boolean isEmpty(EditText myeditText) {
        return myeditText.getText().toString().trim().length() == 0;
    }

    private String capitalize(final String line) {
        return Character.toUpperCase(line.charAt(0)) + line.substring(1);
    }

}
