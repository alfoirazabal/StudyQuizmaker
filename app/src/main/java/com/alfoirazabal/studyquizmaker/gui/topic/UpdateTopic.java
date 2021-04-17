package com.alfoirazabal.studyquizmaker.gui.topic;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.alfoirazabal.studyquizmaker.AppConstants;
import com.alfoirazabal.studyquizmaker.R;
import com.alfoirazabal.studyquizmaker.db.AppDatabase;
import com.alfoirazabal.studyquizmaker.domain.Topic;
import com.alfoirazabal.studyquizmaker.gui.helpers.IconColorPicker;
import com.alfoirazabal.studyquizmaker.helpers.SearchInList;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;

public class UpdateTopic extends AppCompatActivity {

    private TextInputLayout txtilTopicName;
    private TextInputLayout txtilTopicDescription;
    private TextInputEditText txtTopicName;
    private TextInputEditText txtTopicDescription;
    private Button btnUpdate;

    private List<String> topicNames;

    private Topic topic;

    private AppDatabase db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_update);

        txtilTopicName = findViewById(R.id.txtil_topic_name);
        txtilTopicDescription = findViewById(R.id.txtil_topic_description);
        txtTopicName = findViewById(R.id.txt_topic_name);
        txtTopicDescription = findViewById(R.id.txt_topic_description);
        ImageView imgTopicIcon = findViewById(R.id.img_topic_icon);
        Spinner spinnerIconColor = findViewById(R.id.spinner_icon_color);
        btnUpdate = findViewById(R.id.btn_update);

        IconColorPicker iconColorPicker = new IconColorPicker(this);
        iconColorPicker.setIconColorPicker(spinnerIconColor, imgTopicIcon);

        db = Room.databaseBuilder(
                getApplicationContext(),
                AppDatabase.class,
                AppConstants.getDBLocation(getApplicationContext())
        ).build();

        new Thread(() -> {
            Bundle bundle = getIntent().getExtras();
            String topicId = bundle.getString("TOPICID");
            topic = db.topicDAO().getById(topicId);
            topicNames = db.topicDAO().getAllTopicNames(topic.subjectId);
            SearchInList searchInList = new SearchInList(topicNames);
            searchInList.deleteIgnoreCase(topic.name);
            runOnUiThread(() -> {
                txtTopicName.setText(topic.name);
                txtTopicDescription.setText(topic.description);
                iconColorPicker.setCurrentColorInSpinner(topic.color);
                txtilTopicName.setEnabled(true);
                txtilTopicDescription.setEnabled(true);
                btnUpdate.setEnabled(true);
            });
        }).start();

        txtTopicName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                String currentName = s.toString();
                SearchInList searchInList = new SearchInList(topicNames);
                if (searchInList.containsStringIgnoreCase(currentName)) {
                    txtilTopicName.setError(getString(R.string.msg_err_topic_name_exists_already));
                    btnUpdate.setEnabled(false);
                }
                else {
                    txtilTopicName.setError(null);
                    btnUpdate.setEnabled(true);
                }
            }
        });

        btnUpdate.setOnClickListener((view) -> {
            topic.name = txtTopicName.getText().toString();
            topic.description = txtTopicDescription.getText().toString();
            topic.color = iconColorPicker.getSelectedColor().color;
            new Thread(() -> {
                db.topicDAO().update(topic);
                finish();
            }).start();
        });
    }
}
