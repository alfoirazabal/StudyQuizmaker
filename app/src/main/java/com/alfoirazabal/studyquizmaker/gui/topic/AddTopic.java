package com.alfoirazabal.studyquizmaker.gui.topic;

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
import com.alfoirazabal.studyquizmaker.domain.Topic;
import com.alfoirazabal.studyquizmaker.helpers.SearchInList;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;

public class AddTopic extends AppCompatActivity {

    private TextInputLayout txtilTopicName;
    private TextInputLayout txtilTopicDescription;
    private TextInputEditText txtTopicName;
    private TextInputEditText txtTopicDescription;
    private Button btnAdd;

    private List<String> topicNames;

    private String subjectId;

    private AppDatabase db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_add);

        txtilTopicName = findViewById(R.id.txtil_topic_name);
        txtilTopicDescription = findViewById(R.id.txtil_topic_description);
        txtTopicName = findViewById(R.id.txt_topic_name);
        txtTopicDescription = findViewById(R.id.txt_topic_description);
        btnAdd = findViewById(R.id.btn_add);

        db = Room.databaseBuilder(
                getApplicationContext(),
                AppDatabase.class,
                AppConstants.getDBLocation(getApplicationContext())
        ).build();

        new Thread(() -> {
            Bundle bundle = getIntent().getExtras();
            subjectId = bundle.getString("SUBJECTID");
            topicNames = db.topicDAO().getAllTopicNames(subjectId);
            runOnUiThread(() -> {
                txtilTopicName.setEnabled(true);
                txtilTopicDescription.setEnabled(true);
                btnAdd.setEnabled(true);
            });
        }).start();

        txtTopicName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                SearchInList searchInList = new SearchInList(topicNames);
                if (searchInList.containsStringIgnoreCase(s.toString())) {
                    txtilTopicName.setError(getString(R.string.msg_err_topic_name_exists_already));
                    btnAdd.setEnabled(false);
                }
                else {
                    txtilTopicName.setError(null);
                    btnAdd.setEnabled(true);
                }
            }
        });

        btnAdd.setOnClickListener(v -> new Thread(() -> {
            Topic newTopic = new Topic();
            newTopic.subjectId = subjectId;
            newTopic.name = txtTopicName.getText().toString();
            newTopic.description = txtTopicDescription.getText().toString();
            db.topicDAO().insert(newTopic);
            finish();
        }).start());
    }
}
