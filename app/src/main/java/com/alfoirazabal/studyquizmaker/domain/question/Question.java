package com.alfoirazabal.studyquizmaker.domain.question;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.alfoirazabal.studyquizmaker.db.AppDatabase;
import com.alfoirazabal.studyquizmaker.domain.testrun.QuestionResponse;

import java.util.Date;

public interface Question {

    String getTestId();
    String getId();
    String getTitle();
    double getScore();
    String getWrongAnswers() throws NoWrongAnswers;
    String getAnswer();
    String toString(Context context);
    void deleteFromDB(AppDatabase db);
    Class<? extends AppCompatActivity> getUpdateGUIClass();
    QuestionResponse getQuestionResponseObject();
    String getQuestionTypeName(Context context);
    Date getDateCreated();
    Date getDateModified();
    void updateModifiedDate();

    class NoWrongAnswers extends Exception {
        @Nullable
        @Override
        public String getMessage() {
            return "There are no wrong answers to these questions, they should be scored by " +
                    "the player itself";
        }
    }

}
