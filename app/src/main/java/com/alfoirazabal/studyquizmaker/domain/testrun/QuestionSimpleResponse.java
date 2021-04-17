package com.alfoirazabal.studyquizmaker.domain.testrun;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.alfoirazabal.studyquizmaker.db.AppDatabase;
import com.alfoirazabal.studyquizmaker.domain.question.Question;
import com.alfoirazabal.studyquizmaker.domain.question.QuestionSimple;
import com.alfoirazabal.studyquizmaker.gui.test.panel.testrun.answer.AnswerQuestionActivity;
import com.alfoirazabal.studyquizmaker.gui.test.panel.testrun.answer.AnswerQuestionSimple;
import com.alfoirazabal.studyquizmaker.gui.test.panel.testrun.results.questions.ViewQuestionSingleResponse;

import java.io.Serializable;
import java.util.UUID;

@Entity(
        foreignKeys = {
                @ForeignKey(
                        entity = TestRun.class,
                        parentColumns = "id",
                        childColumns = "testRunId",
                        onDelete = ForeignKey.CASCADE,
                        onUpdate = ForeignKey.CASCADE
                ),
                @ForeignKey(
                        entity = QuestionSimple.class,
                        parentColumns = "id",
                        childColumns = "questionSimpleId",
                        onDelete = ForeignKey.CASCADE,
                        onUpdate = ForeignKey.CASCADE
                )
        },
        indices = {
                @Index(value = {"testRunId"}),
                @Index(value = {"questionSimpleId"})
        }
)
public class QuestionSimpleResponse implements QuestionResponse {

    @PrimaryKey
    @NonNull
    public String id;

    @ColumnInfo(name = "testRunId")
    public String testRunId;

    @ColumnInfo(name = "questionSimpleId")
    public String questionSimpleId;

    @ColumnInfo(name = "askOrder")
    public int askOrder;

    @ColumnInfo(name = "answered")
    public String answered;

    @ColumnInfo(name = "score")
    public double score;

    public QuestionSimpleResponse() {
        this.id = UUID.randomUUID().toString();
        this.answered = "";
        this.score = 0;
    }

    @Override
    public String getQuestionId() {
        return this.questionSimpleId;
    }

    @Override
    public boolean isAnswered() {
        return !this.answered.equals("");
    }

    @Override
    public double getScore() {
        return this.score;
    }

    @Override
    public Question getQuestion(AppDatabase db) {
        return db.questionSimpleDAO().getById(this.questionSimpleId);
    }

    @Override
    public String getAnswered(AppDatabase db) {
        return this.answered;
    }

    @Override
    public Class<? extends AnswerQuestionActivity> getAnswerQuestionClass() {
        return AnswerQuestionSimple.class;
    }

    @Override
    public Class<? extends AppCompatActivity> getViewQuestionResponseClass() {
        return ViewQuestionSingleResponse.class;
    }

    @Override
    public int getAskOrder() {
        return this.askOrder;
    }

    @Override
    public void setAskOrder(int askOrder) {
        this.askOrder = askOrder;
    }

    @Override
    public void setTestRunId(String testRunId) {
        this.testRunId = testRunId;
    }

    @Override
    public void setQuestion(Question question) {
        this.questionSimpleId = question.getId();
    }

    @Override
    public void insertToDb(AppDatabase db) {
        db.questionSimpleResponseDAO().insert(this);
    }
}
