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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
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
public class QuestionMC implements Question {

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
    public QuestionOptionMC[] questionOptionMCs;

    @Ignore
    public QuestionOptionMC rightOption;

    public QuestionMC() {
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
        db.questionMCDAO().delete(this);
    }

    @Override
    public Class<? extends AppCompatActivity> getUpdateGUIClass() {
        throw new UnsupportedOperationException("No Update GUI for MC Question yet!");
    }

    @Override
    public QuestionResponse getQuestionResponseObject() {
        throw new UnsupportedOperationException("No Response object for MC Question yet!");
    }

    @Override
    public String getTestId() {
        return this.testId;
    }

    @NonNull
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

    @Override
    public String getWrongAnswers() {
        if (this.rightOption == null) {
            setRightOption();
        }
        List<QuestionOptionMC> wrongOptions = new ArrayList<>();
        wrongOptions.addAll(Arrays.asList(questionOptionMCs));
        wrongOptions.remove(this.rightOption);
        String textWrongOptions = "";
        for (int i = 0 ; i < wrongOptions.size() ; i++) {
            textWrongOptions += wrongOptions.get(i).answerText;
            if (i != wrongOptions.size() - 1) {
                textWrongOptions += "\n";
            }
        }
        return  textWrongOptions;
    }

    @Override
    public String getQuestionTypeName(Context context) {
        return context.getString(R.string.questions_multiple_choice);
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
