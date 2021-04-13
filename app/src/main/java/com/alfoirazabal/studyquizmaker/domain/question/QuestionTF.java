package com.alfoirazabal.studyquizmaker.domain.question;


import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.alfoirazabal.studyquizmaker.R;
import com.alfoirazabal.studyquizmaker.db.AppDatabase;
import com.alfoirazabal.studyquizmaker.domain.Test;
import com.alfoirazabal.studyquizmaker.domain.testrun.QuestionResponse;
import com.alfoirazabal.studyquizmaker.domain.testrun.QuestionTFResponse;
import com.alfoirazabal.studyquizmaker.gui.test.panel.questions.questionsimple.UpdateQuestionSimple;
import com.alfoirazabal.studyquizmaker.gui.test.panel.questions.questiontf.UpdateQuestionTF;

import java.util.Date;
import java.util.UUID;

@Entity(
        foreignKeys = {
                @ForeignKey(
                        entity = Test.class,
                        parentColumns = "id",
                        childColumns = "testId",
                        onDelete = ForeignKey.CASCADE,
                        onUpdate = ForeignKey.CASCADE
                )
        },
        indices = {
                @Index(value = {"testId"}),
                @Index(value = {"testId", "dateCreated"}),
                @Index(value = {"testId", "dateModified"})
        }
)
public class QuestionTF implements Question {

    @PrimaryKey
    @NonNull
    public String id;

    @ColumnInfo(name = "testId")
    public String testId;

    @ColumnInfo(name = "title")
    public String title;

    @ColumnInfo(name = "answerTrue")
    public String answerTrue;

    @ColumnInfo(name = "answerFalse")
    public String answerFalse;

    @ColumnInfo(name = "score")
    public double score;

    @ColumnInfo(name = "dateCreated")
    public Date dateCreated;

    @ColumnInfo(name = "dateModified")
    public Date dateModified;

    public QuestionTF() {
        this.id = UUID.randomUUID().toString();
        this.dateCreated = new Date();
        this.dateModified = this.dateCreated;
    }

    @Override
    public String toString(Context context) {
        return context.getString(R.string.title) + ": " + this.title;
    }

    @Override
    public void deleteFromDB(AppDatabase db) {
        db.questionTFDAO().delete(this);
    }

    @Override
    public Class<?> getUpdateGUIClass() {
        return UpdateQuestionTF.class;
    }

    @Override
    public QuestionResponse getQuestionResponseObject() {
        return new QuestionTFResponse();
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
        return this.score;
    }

    @Override
    public String getAnswer() {
        return this.answerTrue;
    }

    @Override
    public String getWrongAnswers() {
        return this.answerFalse;
    }

    @Override
    public String getQuestionTypeName(Context context) {
        return context.getString(R.string.questions_true_or_false);
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
