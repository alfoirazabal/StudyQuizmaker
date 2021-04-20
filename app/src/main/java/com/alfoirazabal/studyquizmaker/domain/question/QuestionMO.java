package com.alfoirazabal.studyquizmaker.domain.question;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.alfoirazabal.studyquizmaker.R;
import com.alfoirazabal.studyquizmaker.db.AppDatabase;
import com.alfoirazabal.studyquizmaker.domain.Test;
import com.alfoirazabal.studyquizmaker.domain.testrun.QuestionMOResponse;
import com.alfoirazabal.studyquizmaker.domain.testrun.QuestionResponse;
import com.alfoirazabal.studyquizmaker.helpers.IdGenerator;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Entity(
        foreignKeys = {
                @ForeignKey(
                        entity = Test.class,
                        parentColumns = "id",
                        childColumns = "testId",
                        onUpdate = ForeignKey.CASCADE,
                        onDelete = ForeignKey.CASCADE
                )
        },
        indices = {
                @Index(value = {"testId"}),
                @Index(value = {"testId", "dateCreated"}),
                @Index(value = {"testId", "dateModified"})
        }
)
public class QuestionMO implements Question {

    @PrimaryKey
    @NonNull
    public String id;

    @ColumnInfo(name = "testId")
    public String testId;

    @ColumnInfo(name = "title")
    public String title;

    @ColumnInfo(name = "dateCreated")
    public Date dateCreated;

    @ColumnInfo(name = "dateModified")
    public Date dateModified;

    @Ignore
    public QuestionOptionMO[] questionOptionMOs;

    public QuestionMO() {
        this.id = IdGenerator.generateId();
        this.dateCreated = new Date();
        this.dateModified = this.dateCreated;
    }

    @Override
    public String getTestId() {
        return this.testId;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public String getTitle() {
        return this.title;
    }

    @Override
    public double getScore() {
        double score = 0;
        for (QuestionOptionMO questionOptionMO : this.questionOptionMOs) {
            if (questionOptionMO.score > 0) {
                score += questionOptionMO.score;
            }
        }
        return score;
    }

    @Override
    public String getWrongAnswers() throws NoWrongAnswers {
        List<String> wrongAnswers = new ArrayList<>();
        for (QuestionOptionMO questionOptionMO : this.questionOptionMOs) {
            if (questionOptionMO.score <= 0) {
                wrongAnswers.add(questionOptionMO.answerText);
            }
        }
        StringBuilder wrongAnswersText = new StringBuilder();
        Iterator<String> itWrongAnswers = wrongAnswers.iterator();
        while (itWrongAnswers.hasNext()) {
            String wrongAnswer = itWrongAnswers.next();
            wrongAnswersText.append(wrongAnswer);
            if (itWrongAnswers.hasNext()) {
                wrongAnswersText.append("\n");
            }
        }
        return wrongAnswersText.toString();
    }

    @Override
    public String getAnswer() {
        List<String> rightAnswers = new ArrayList<>();
        for (QuestionOptionMO questionOptionMO : this.questionOptionMOs) {
            if (questionOptionMO.score > 0) {
                rightAnswers.add(questionOptionMO.answerText);
            }
        }
        StringBuilder answers = new StringBuilder();
        Iterator<String> itRightAnswers = rightAnswers.iterator();
        while (itRightAnswers.hasNext()) {
            String answer = itRightAnswers.next();
            answers.append(answer);
            if (itRightAnswers.hasNext()) {
                answers.append("\n");
            }
        }
        return answers.toString();
    }

    @Override
    public String toString(Context context) {
        return context.getString(R.string.title) + ": " + this.title;
    }

    @Override
    public void deleteFromDB(AppDatabase db) {
        db.questionMODAO().delete(this);
    }

    @Override
    public Class<? extends AppCompatActivity> getUpdateGUIClass() {
        throw new UnsupportedOperationException("No Update GUI for MO Question yet!");
    }

    @Override
    public QuestionResponse getQuestionResponseObject() {
        return new QuestionMOResponse();
    }

    @Override
    public String getQuestionTypeName(Context context) {
        return context.getString(R.string.questions_multiple_options);
    }

    @Override
    public Date getDateCreated() {
        return this.dateCreated;
    }

    @Override
    public Date getDateModified() {
        return this.dateModified;
    }

    @Override
    public void updateModifiedDate() {
        this.dateModified = new Date();
    }
}
