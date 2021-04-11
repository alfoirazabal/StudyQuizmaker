package com.alfoirazabal.studyquizmaker.domain.question;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
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
public class QuestionMC implements Question {

    @PrimaryKey
    @NonNull
    public String id;

    @ColumnInfo(name = "testId")
    public String testId;

    @ColumnInfo(name = "title")
    public String title;

    @ColumnInfo(name = "description")
    public String description;

    @Ignore
    public QuestionOptionMC[] questionOptionMCs;

    @Ignore
    public QuestionOptionMC rightOption;

    public QuestionMC() {
        this.id = UUID.randomUUID().toString();
    }

    @Override
    public String toString(Context context) {
        return context.getString(R.string.title) + ": " + this.title;
    }

    @Override
    public void deleteFromDB(AppDatabase db) {
        db.questionMCDAO().delete(this);
    }

    @Override
    public Class<UpdateQuestionSimple> getUpdateGUIClass() {
        throw new UnsupportedOperationException("No Update GUI for MC Question yet!");
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
        if (this.rightOption == null) {
            setRightOption();
        }
        return this.rightOption.score;
    }

    @Override
    public String getAnswer() {
        if (this.rightOption == null) {
            setRightOption();
        }
        return this.rightOption.answerText;
    }

    private void setRightOption() {
        if (questionOptionMCs == null) {
            throw new Error("QuestionMO options not set");
        }
        if (questionOptionMCs.length == 0) {
            throw new Error("There are no QuestionMO options to determine right option");
        }
        QuestionOptionMC maxScoredOption = questionOptionMCs[0];
        for (int i = 1 ; i < questionOptionMCs.length ; i++) {
            if (questionOptionMCs[i].score > maxScoredOption.score) {
                maxScoredOption = questionOptionMCs[i];
            }
        }
        this.rightOption = maxScoredOption;
    }
}
