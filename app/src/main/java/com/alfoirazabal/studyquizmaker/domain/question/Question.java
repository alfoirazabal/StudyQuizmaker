package com.alfoirazabal.studyquizmaker.domain.question;

import android.content.Context;

import androidx.annotation.Nullable;

import com.alfoirazabal.studyquizmaker.db.AppDatabase;

public interface Question {

    String getTestId();
    String getId();
    String getTitle();
    double getScore();
    String getWrongAnswers() throws NoWrongAnswers;
    String getAnswer();
    String toString(Context context);
    void deleteFromDB(AppDatabase db);
    Class<?> getUpdateGUIClass();
    String getQuestionTypeName(Context context);

    class NoWrongAnswers extends Exception {
        @Nullable
        @Override
        public String getMessage() {
            return "There are no wrong answers to these questions, they should be scored by " +
                    "the player itself";
        }
    }

}
