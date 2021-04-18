package com.alfoirazabal.studyquizmaker.gui.test.panel.testrun.answer.guiextensions;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.CheckBox;

import androidx.annotation.NonNull;

import com.alfoirazabal.studyquizmaker.domain.question.QuestionOptionMO;

public class MOCheckBox extends androidx.appcompat.widget.AppCompatCheckBox {

    private QuestionOptionMO questionOptionMO;

    public MOCheckBox(Context context) {
        super(context);
    }

    public void setQuestionOptionMO(QuestionOptionMO questionOptionMO) {
        this.questionOptionMO = questionOptionMO;
        this.setText(questionOptionMO.answerText);
    }

    public QuestionOptionMO getQuestionOptionMO() {
        return this.questionOptionMO;
    }

}
