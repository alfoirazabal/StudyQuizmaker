package com.alfoirazabal.studyquizmaker.gui.test.panel.testrun.answer.guiextensions;

import android.content.Context;
import android.util.AttributeSet;

import com.alfoirazabal.studyquizmaker.domain.question.QuestionOptionMC;

public class MCRadioButton extends androidx.appcompat.widget.AppCompatRadioButton {

    private QuestionOptionMC questionOptionMC;

    public MCRadioButton(Context context) {
        super(context);
    }

    public void setQuestionOptionMC(QuestionOptionMC questionOptionMC) {
        this.questionOptionMC = questionOptionMC;
        this.setText(questionOptionMC.answerText);
    }

    public QuestionOptionMC getQuestionOptionMC() {
        return this.questionOptionMC;
    }
}
