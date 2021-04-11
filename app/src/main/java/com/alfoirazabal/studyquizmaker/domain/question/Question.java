package com.alfoirazabal.studyquizmaker.domain.question;

import android.content.Context;

import com.alfoirazabal.studyquizmaker.db.AppDatabase;
import com.alfoirazabal.studyquizmaker.gui.test.panel.questions.UpdateGUIClass;
import com.alfoirazabal.studyquizmaker.gui.test.panel.questions.questionsimple.UpdateQuestionSimple;

public interface Question {

    String getTestId();
    String getId();
    String getTitle();
    double getScore();
    String getAnswer();
    String toString(Context context);
    void deleteFromDB(AppDatabase db);
    Class<?> getUpdateGUIClass();

}
