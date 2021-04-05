package com.alfoirazabal.studyquizmaker.gui.test;

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
import com.alfoirazabal.studyquizmaker.domain.Test;
import com.alfoirazabal.studyquizmaker.helpers.SearchInList;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;
import java.util.Objects;

public class UpdateTest extends AppCompatActivity {

    private TextInputLayout txtilTestName;
    private TextInputLayout txtilTestDescription;
    private TextInputEditText txtTestName;
    private TextInputEditText txtTestDescription;
    private Button btnUpdate;

    private List<String> testNames;

    private Test currentTest;

    private AppDatabase db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_update);

        txtilTestName = findViewById(R.id.txtil_test_name);
        txtilTestDescription = findViewById(R.id.txtil_test_description);
        txtTestName = findViewById(R.id.txt_test_name);
        txtTestDescription = findViewById(R.id.txt_test_description);
        btnUpdate = findViewById(R.id.btn_update);

        db = Room.databaseBuilder(
                getApplicationContext(),
                AppDatabase.class,
                AppConstants.DATABASE_LOCATION
        ).build();

        new Thread(() -> {
            Bundle bundle = getIntent().getExtras();
            String currentTestId = bundle.getString("TESTID");
            currentTest = db.testDAO().getById(currentTestId);
            testNames = db.testDAO().getAllTestNames(currentTest.topicId);
            SearchInList searchInList = new SearchInList(testNames);
            searchInList.deleteIgnoreCase(currentTest.name);
            runOnUiThread(() -> {
                txtTestName.setText(currentTest.name);
                txtTestDescription.setText(currentTest.description);
                txtilTestName.setEnabled(true);
                txtilTestDescription.setEnabled(true);
                btnUpdate.setEnabled(true);
            });
        }).start();

        txtTestName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                String currentName = s.toString();
                SearchInList searchInList = new SearchInList(testNames);
                if (searchInList.containsStringIgnoreCase(currentName)) {
                    txtilTestName.setError(getString(R.string.msg_err_test_name_exists_already));
                    btnUpdate.setEnabled(false);
                }
                else {
                    txtilTestName.setError(null);
                    btnUpdate.setEnabled(true);
                }
            }
        });

        btnUpdate.setOnClickListener(v -> {
            currentTest.name = Objects.requireNonNull(txtTestName.getText()).toString();
            currentTest.description = Objects.requireNonNull(
                    txtTestDescription.getText()
            ).toString();
            new Thread(() -> {
                db.testDAO().update(currentTest);
                finish();
            }).start();
        });
    }
}
