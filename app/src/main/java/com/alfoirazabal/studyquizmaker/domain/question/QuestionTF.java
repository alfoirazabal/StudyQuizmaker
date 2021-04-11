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
import com.alfoirazabal.studyquizmaker.gui.test.panel.questions.questionsimple.UpdateQuestionSimple;

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
                @Index(value = {"testId"})
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

    @ColumnInfo(name = "description")
    public String description;

    @ColumnInfo(name = "answerTrue")
    public String answerTrue;

    @ColumnInfo(name = "answerFalse")
    public String answerFalse;

    @ColumnInfo(name = "score")
    public double score;

    public QuestionTF() {
        this.id = UUID.randomUUID().toString();
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
        throw new UnsupportedOperationException("No update GUI for TF Question yet!");
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
}
