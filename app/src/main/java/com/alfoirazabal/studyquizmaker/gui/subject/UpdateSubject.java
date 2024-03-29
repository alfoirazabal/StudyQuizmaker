package com.alfoirazabal.studyquizmaker.gui.subject;

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
import com.alfoirazabal.studyquizmaker.domain.Subject;
import com.alfoirazabal.studyquizmaker.gui.helpers.IconColorPicker;
import com.alfoirazabal.studyquizmaker.helpers.SearchInList;
import com.alfoirazabal.studyquizmaker.helpers.icons.IconColors;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;
import java.util.Objects;

public class UpdateSubject extends AppCompatActivity {

    private TextInputLayout txtilSubjectName;
    private TextInputLayout txtilSubjectDescription;
    private TextInputEditText txtSubjectName;
    private TextInputEditText txtSubjectDescription;
    private Spinner spinnerIconColor;
    private ImageView imgSubjectIcon;
    private Button btnUpdate;

    private List<String> subjectNames;
    private Subject subject;

    private AppDatabase db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_update);

        txtilSubjectName = findViewById(R.id.txtil_subject_name);
        txtilSubjectDescription = findViewById(R.id.txtil_subject_description);
        txtSubjectName = findViewById(R.id.txt_subject_name);
        txtSubjectDescription = findViewById(R.id.txt_subject_description);
        spinnerIconColor = findViewById(R.id.spinner_icon_color);
        imgSubjectIcon = findViewById(R.id.img_subject_icon);
        btnUpdate = findViewById(R.id.btn_update);

        IconColorPicker iconColorPicker = new IconColorPicker(this);
        iconColorPicker.setIconColorPicker(spinnerIconColor, imgSubjectIcon);

        db = Room.databaseBuilder(
                getApplicationContext(),
                AppDatabase.class,
                AppConstants.getDBLocation(getApplicationContext())
        ).build();

        new Thread(() -> {
            Bundle bundle = getIntent().getExtras();
            String subjectId = bundle.getString("SUBJECTID");
            this.subject = db.subjectDAO().getById(subjectId);
            this.subjectNames = db.subjectDAO().getAllSubjectNames();
            SearchInList searchInList = new SearchInList(this.subjectNames);
            searchInList.deleteIgnoreCase(subject.name);
            runOnUiThread(() -> {
                txtSubjectName.setText(this.subject.name);
                txtSubjectDescription.setText(this.subject.description);
                iconColorPicker.setCurrentColorInSpinner(this.subject.color);
                txtilSubjectName.setEnabled(true);
                txtilSubjectDescription.setEnabled(true);
                btnUpdate.setEnabled(true);
            });
        }).start();

        txtSubjectName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                SearchInList searchInList = new SearchInList(subjectNames);
                if (searchInList.containsStringIgnoreCase(s.toString())) {
                    txtilSubjectName.setError(getString(
                            R.string.msg_err_subject_name_exists_already)
                    );
                    btnUpdate.setEnabled(false);
                }
                else {
                    txtilSubjectName.setError(null);
                    btnUpdate.setEnabled(true);
                }
            }
        });

        btnUpdate.setOnClickListener((view) -> {
            subject.name = Objects.requireNonNull(txtSubjectName.getText()).toString();
            subject.description = Objects.requireNonNull(
                    txtSubjectDescription.getText()
            ).toString();
            IconColors.ColorForIcon colorSelected = iconColorPicker.getSelectedColor();
            subject.color = colorSelected.color;
            new Thread(() -> {
                db.subjectDAO().update(subject);
                finish();
            }).start();
        });
    }
}
