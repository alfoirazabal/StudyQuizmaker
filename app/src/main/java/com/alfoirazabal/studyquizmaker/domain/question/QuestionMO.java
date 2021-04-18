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
import com.alfoirazabal.studyquizmaker.domain.testrun.QuestionResponse;

import java.util.Date;
import java.util.UUID;

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
        this.id = UUID.randomUUID().toString();
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
            score += questionOptionMO.score;
        }
        return score;
    }

    @Override
    public String getWrongAnswers() throws NoWrongAnswers {
        StringBuilder wrongAnswers = new StringBuilder();
        for (int i = 0 ; i < this.questionOptionMOs.length ; i++) {
            if (this.questionOptionMOs[i].score <= 0) {
                wrongAnswers.append(this.questionOptionMOs[i].answerText);
                if (i != this.questionOptionMOs.length - 1) {
                    wrongAnswers.append("\n");
                }
            }
        }
        return wrongAnswers.toString();
    }

    @Override
    public String getAnswer() {
        StringBuilder answers = new StringBuilder();
        for (int i = 0 ; i < this.questionOptionMOs.length ; i++) {
            if (this.questionOptionMOs[i].score > 0) {
                answers.append(this.questionOptionMOs[i].answerText);
                if (i != this.questionOptionMOs.length - 1) {
                    answers.append("\n");
                }
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
        throw new UnsupportedOperationException("Not yet implemeted!");
    }

    @Override
    public QuestionResponse getQuestionResponseObject() {
        throw new UnsupportedOperationException("No response object yet!");
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
