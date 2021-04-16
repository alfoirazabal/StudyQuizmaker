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
import com.alfoirazabal.studyquizmaker.domain.question.QuestionMC;
import com.alfoirazabal.studyquizmaker.domain.question.QuestionOptionMC;
import com.alfoirazabal.studyquizmaker.domain.question.QuestionSimple;
import com.alfoirazabal.studyquizmaker.gui.test.panel.testrun.answer.AnswerQuestionActivity;
import com.alfoirazabal.studyquizmaker.gui.test.panel.testrun.answer.AnswerQuestionMC;

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
                        entity = QuestionMC.class,
                        parentColumns = "id",
                        childColumns = "questionMCId",
                        onDelete = ForeignKey.CASCADE,
                        onUpdate = ForeignKey.CASCADE
                )
        },
        indices = {
                @Index(value = {"testRunId"}),
                @Index(value = {"questionMCId"})
        }
)
public class QuestionMCResponse implements QuestionResponse {

    @PrimaryKey
    @NonNull
    public String id;

    @ColumnInfo(name = "testRunId")
    public String testRunId;

    @ColumnInfo(name = "questionMCId")
    public String questionMCId;

    @ColumnInfo(name = "askOrder")
    public int askOrder;

    @ColumnInfo(name = "questionMCOptionSelected")
    public String questionMCOptionSelected;

    @ColumnInfo(name = "score")
    public double score;

    public QuestionMCResponse() {
        this.id = UUID.randomUUID().toString();
    }

    @Override
    public String getQuestionId() {
        return this.questionMCId;
    }

    @Override
    public boolean isAnswered() {
        return this.questionMCOptionSelected != null;
    }

    @Override
    public double getScore() {
        return this.score;
    }

    @Override
    public Question getQuestion(AppDatabase db) {
        QuestionMC question = db.questionMCDAO().getById(this.questionMCId);
        question.questionOptionMCs = db.questionOptionMCDAO().getFromQuestionMC(this.questionMCId)
                .toArray(new QuestionOptionMC[0]);
        return question;
    }

    @Override
    public String getAnswered(AppDatabase db) {
        return db.questionOptionMCDAO().getById(this.questionMCOptionSelected).answerText;
    }

    @Override
    public Class<? extends AnswerQuestionActivity> getAnswerQuestionClass() {
        return AnswerQuestionMC.class;
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
    public void setQuestionId(String questionId) {
        this.questionMCId = questionId;
    }

    @Override
    public void insertToDb(AppDatabase db) {
        db.questionMCResponseDAO().insert(this);
    }
}
