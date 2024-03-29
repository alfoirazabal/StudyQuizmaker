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
import com.alfoirazabal.studyquizmaker.domain.question.QuestionTF;
import com.alfoirazabal.studyquizmaker.gui.test.panel.testrun.answer.AnswerQuestionActivity;
import com.alfoirazabal.studyquizmaker.gui.test.panel.testrun.answer.AnswerQuestionTF;
import com.alfoirazabal.studyquizmaker.gui.test.panel.testrun.results.questions.ViewQuestionTFResponse;
import com.alfoirazabal.studyquizmaker.helpers.IdGenerator;

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
                        entity = QuestionTF.class,
                        parentColumns = "id",
                        childColumns = "questionTFId",
                        onDelete = ForeignKey.CASCADE,
                        onUpdate = ForeignKey.CASCADE
                )
        },
        indices = {
                @Index(value = {"testRunId"}),
                @Index(value = {"questionTFId"})
        }
)
public class QuestionTFResponse implements QuestionResponse {

    @PrimaryKey
    @NonNull
    public String id;

    @ColumnInfo(name = "testRunId")
    public String testRunId;

    @ColumnInfo(name = "questionTFId")
    public String questionTFId;

    @ColumnInfo(name = "askOrder")
    public int askOrder;

    @ColumnInfo(name = "isAnswered")
    public boolean isAnswered;

    @ColumnInfo(name = "askedTrueStatement")
    public boolean askedTrueStatement;

    @ColumnInfo(name = "answeredCorrectly")
    public boolean answeredCorrectly;

    @ColumnInfo(name = "score")
    public double score;

    public QuestionTFResponse() {
        this.id = IdGenerator.generateId();
        if (Math.random() > 0.5) {
            this.askedTrueStatement = true;
        }
        else {
            this.askedTrueStatement = false;
        }
    }

    @Override
    public String getQuestionId() {
        return this.id;
    }

    @Override
    public boolean isAnswered() {
        return this.isAnswered;
    }

    @Override
    public double getScore() {
        return this.score;
    }

    @Override
    public Question getQuestion(AppDatabase db) {
        return db.questionTFDAO().getById(this.questionTFId);
    }

    @Override
    public String getAnswered(AppDatabase db) {
        String answered;
        if (this.answeredCorrectly) {
            answered = db.questionTFDAO().getById(this.questionTFId).answerTrue;
        }
        else {
            answered = db.questionTFDAO().getById(this.questionTFId).answerFalse;
        }
        return answered;
    }

    @Override
    public Class<? extends AnswerQuestionActivity> getAnswerQuestionClass() {
        return AnswerQuestionTF.class;
    }

    @Override
    public Class<? extends AppCompatActivity> getViewQuestionResponseClass() {
        return ViewQuestionTFResponse.class;
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
        this.questionTFId = question.getId();
    }

    @Override
    public void insertToDb(AppDatabase db) {
        db.questionTFResponseDAO().insert(this);
    }
}
