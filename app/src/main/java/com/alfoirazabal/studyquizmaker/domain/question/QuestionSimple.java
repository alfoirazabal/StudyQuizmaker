package com.alfoirazabal.studyquizmaker.domain.question;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.alfoirazabal.studyquizmaker.R;
import com.alfoirazabal.studyquizmaker.db.AppDatabase;
import com.alfoirazabal.studyquizmaker.domain.Test;
import com.alfoirazabal.studyquizmaker.domain.testrun.QuestionResponse;
import com.alfoirazabal.studyquizmaker.domain.testrun.QuestionSimpleResponse;
import com.alfoirazabal.studyquizmaker.gui.test.panel.questions.questionsimple.UpdateQuestionSimple;
import com.alfoirazabal.studyquizmaker.helpers.IdGenerator;

import java.util.Date;

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
public class QuestionSimple implements Question {

    @PrimaryKey
    @NonNull
    public String id;

    @ColumnInfo(name = "testId")
    public String testId;

    @ColumnInfo(name = "title")
    public String title;

    @ColumnInfo(name = "answer")
    public String answer;

    @ColumnInfo(name = "score")
    public double score;

    @ColumnInfo(name = "dateCreated")
    public Date dateCreated;

    @ColumnInfo(name = "dateModified")
    public Date dateModified;

    public QuestionSimple() {
        this.id = IdGenerator.generateId();
        this.dateCreated = new Date();
        this.dateModified = this.dateCreated;
    }

    @Override
    public String toString(Context context) {
        return context.getString(R.string.title) + ": " + this.title;
    }

    @Override
    public void deleteFromDB(AppDatabase db) {
        db.questionSimpleDAO().delete(this);
    }

    @Override
    public Class<? extends AppCompatActivity> getUpdateGUIClass() {
        return UpdateQuestionSimple.class;
    }

    @Override
    public QuestionResponse getQuestionResponseObject() {
        return new QuestionSimpleResponse();
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
        return this.answer;
    }

    @Override
    public String getWrongAnswers() throws NoWrongAnswers {
        throw new NoWrongAnswers();
    }

    @Override
    public String getQuestionTypeName(Context context) {
        return context.getString(R.string.simple_question);
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
