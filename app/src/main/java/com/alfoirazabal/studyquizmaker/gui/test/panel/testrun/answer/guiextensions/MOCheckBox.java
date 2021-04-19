package com.alfoirazabal.studyquizmaker.gui.test.panel.testrun.answer.guiextensions;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.alfoirazabal.studyquizmaker.domain.question.QuestionOptionMO;

public class MOCheckBox extends LinearLayout {

    private TextView txtScore;
    private CheckBox checkBox;
    private QuestionOptionMO questionOptionMO;
    private boolean canViewScore;

    public MOCheckBox(Context context) {
        super(context);
        this.canViewScore = false;
        this.setOrientation(HORIZONTAL);
        this.txtScore = new TextView(context);
        this.checkBox = new CheckBox(context);
        this.addView(this.txtScore);
        this.addView(this.checkBox);
    }

    public void setCanViewScore(boolean canViewScore) {
        this.canViewScore = canViewScore;
    }

    public void setQuestionOptionMO(QuestionOptionMO questionOptionMO) {
        this.questionOptionMO = questionOptionMO;
        this.checkBox.setText(questionOptionMO.answerText);
        if (this.canViewScore) {
            this.txtScore.setText(String.valueOf(questionOptionMO.score));
        }
    }

    public void setChecked(boolean checked) {
        this.checkBox.setChecked(checked);
    }

    public boolean isChecked() {
        return this.checkBox.isChecked();
    }

    public void setTextColor(int color) {
        this.checkBox.setTextColor(color);
    }

    public void setOnCheckChangedListener(CompoundButton.OnCheckedChangeListener onCheckChangedListener) {
        this.checkBox.setOnCheckedChangeListener(onCheckChangedListener);
    }

    public QuestionOptionMO getQuestionOptionMO() {
        return this.questionOptionMO;
    }

}
