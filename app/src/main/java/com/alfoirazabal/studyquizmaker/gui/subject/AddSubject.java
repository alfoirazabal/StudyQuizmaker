package com.alfoirazabal.studyquizmaker.gui.subject;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.alfoirazabal.studyquizmaker.AppConstants;
import com.alfoirazabal.studyquizmaker.R;
import com.alfoirazabal.studyquizmaker.db.AppDatabase;
import com.alfoirazabal.studyquizmaker.domain.Subject;
import com.alfoirazabal.studyquizmaker.helpers.SearchInList;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;
import java.util.Objects;

public class AddSubject extends AppCompatActivity {

    private TextInputLayout txtilSubjectName;
    private TextInputLayout txtilSubjectDescription;
    private TextInputEditText txtSubjectName;
    private TextInputEditText txtSubjectDescription;
    private Button btnAdd;

    private List<String> subjectNames;

    private AppDatabase db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_add);

        txtilSubjectName = findViewById(R.id.txtil_subject_name);
        txtilSubjectDescription = findViewById(R.id.txtil_subject_description);
        txtSubjectName = findViewById(R.id.txt_subject_name);
        txtSubjectDescription = findViewById(R.id.txt_subject_description);
        btnAdd = findViewById(R.id.btn_add);

        db = Room.databaseBuilder(
                getApplicationContext(),
                AppDatabase.class,
                AppConstants.DATABASE_LOCATION
        ).build();

        new Thread(() -> {
            subjectNames = db.subjectDAO().getAllSubjectNames();
            btnAdd.setEnabled(true);
            txtilSubjectDescription.setEnabled(true);
            txtilSubjectName.setEnabled(true);
        }).start();

        txtSubjectName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                String currentSubjectName = s.toString();
                SearchInList searchInList = new SearchInList(subjectNames);
                if (searchInList.containsStringIgnoreCase(currentSubjectName)) {
                    btnAdd.setEnabled(false);
                    txtilSubjectName.setError(getString(R.string.msg_err_subject_name_exists_already));
                }
                else {
                    btnAdd.setEnabled(true);
                    txtilSubjectName.setError(null);
                }
            }
        });

        btnAdd.setOnClickListener(v -> new Thread(() -> {
            Subject newSubject = new Subject();
            newSubject.name = Objects.requireNonNull(txtSubjectName.getText()).toString();
            newSubject.description = Objects.requireNonNull(
                    txtSubjectDescription.getText()
            ).toString();
            db.subjectDAO().insert(newSubject);
            finish();
        }).start());

    }

}
