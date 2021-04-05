package com.alfoirazabal.studyquizmaker.gui.test;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class AddTest extends AppCompatActivity {

    private TextInputLayout txtilTestName;
    private TextInputLayout txtilTestDescription;
    private TextInputEditText txtTestName;
    private TextInputEditText txtTestDescription;
    private Button btnAdd;

    private String topicId;

    private List<String> testNames;

    private AppDatabase db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_add);

        txtilTestName = findViewById(R.id.txtil_test_name);
        txtilTestDescription = findViewById(R.id.txtil_test_description);
        txtTestName = findViewById(R.id.txt_test_name);
        txtTestDescription = findViewById(R.id.txt_test_description);
        btnAdd = findViewById(R.id.btn_add);

        db = Room.databaseBuilder(
                getApplicationContext(),
                AppDatabase.class,
                AppConstants.getDBLocation(getApplicationContext())
        ).build();

        new Thread(() -> {
            Bundle bundle = getIntent().getExtras();
            this.topicId = bundle.getString("TOPICID");
            testNames = db.testDAO().getAllTestNames(this.topicId);
            runOnUiThread(() -> {
                txtilTestName.setEnabled(true);
                txtilTestDescription.setEnabled(true);
                btnAdd.setEnabled(true);
            });
        }).start();

        txtTestName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                String currentText = s.toString();
                SearchInList searchInList = new SearchInList(testNames);
                if (searchInList.containsStringIgnoreCase(currentText)) {
                    txtilTestName.setError(getString(R.string.msg_err_test_name_exists_already));
                    btnAdd.setEnabled(false);
                }
                else {
                    txtilTestName.setError(null);
                    btnAdd.setEnabled(true);
                }
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(() -> {
                    Test test = new Test();
                    test.name = txtTestName.getText().toString();
                    test.description = txtTestDescription.getText().toString();
                    test.topicId = topicId;
                    db.testDAO().insert(test);
                    finish();
                }).start();
            }
        });
    }
}
