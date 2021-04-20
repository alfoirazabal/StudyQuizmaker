package com.alfoirazabal.studyquizmaker.domain.testrun;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.alfoirazabal.studyquizmaker.db.AppDatabase;
import com.alfoirazabal.studyquizmaker.domain.question.Question;
import com.alfoirazabal.studyquizmaker.domain.question.QuestionMO;
import com.alfoirazabal.studyquizmaker.domain.question.QuestionOptionMO;
import com.alfoirazabal.studyquizmaker.gui.test.panel.testrun.answer.AnswerQuestionActivity;
import com.alfoirazabal.studyquizmaker.gui.test.panel.testrun.answer.AnswerQuestionMO;
import com.alfoirazabal.studyquizmaker.gui.test.panel.testrun.results.questions.ViewQuestionMOResponse;
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
                        entity = QuestionMO.class,
                        parentColumns = "id",
                        childColumns = "questionMOId",
                        onDelete = ForeignKey.CASCADE,
                        onUpdate = ForeignKey.CASCADE
                )
        },
        indices = {
                @Index(value = {"testRunId"}),
                @Index(value = {"questionMOId"})
        }
)
public class QuestionMOResponse implements QuestionResponse {

    @PrimaryKey
    @NonNull
    public String id;

    @ColumnInfo(name = "testRunId")
    public String testRunId;

    @ColumnInfo(name = "questionMOId")
    public String questionMOId;

    @ColumnInfo(name = "askOrder")
    public int askOrder;

    @Ignore
    public QuestionMOResponseOption[] questionMOResponseOptions;

    @Ignore
    public QuestionOptionMO[] questionOptionMOs;

    public QuestionMOResponse() {
        this.id = IdGenerator.generateId();
    }

    @Override
    public String getQuestionId() {
        return this.questionMOId;
    }

    @Override
    public boolean isAnswered() {
        boolean answered = false;
        for (int i = 0 ; !answered && i < this.questionMOResponseOptions.length ; i++) {
            answered = this.questionMOResponseOptions[i].optionSelected;
        }
        return answered;
    }

    @Override
    public double getScore() {
        double score = 0;
        for (QuestionMOResponseOption questionMOResponseOption : questionMOResponseOptions) {
            if (questionMOResponseOption.optionSelected) {
                for (QuestionOptionMO questionOptionMO : questionOptionMOs) {
                    if (questionOptionMO.id.equals(questionMOResponseOption.questionOptionMOId)) {
                        score += questionOptionMO.score;
                    }
                }
            }
        }
        return score;
    }

    @Override
    public Question getQuestion(AppDatabase db) {
        QuestionMO question = db.questionMODAO().getById(this.questionMOId);
        question.questionOptionMOs = db.questionOptionMODAO().getFromQuestionMO(this.questionMOId)
                .toArray(new QuestionOptionMO[0]);
        return question;
    }

    @Override
    public String getAnswered(AppDatabase db) {
        this.questionOptionMOs = db.questionOptionMODAO().getFromQuestionMO(this.questionMOId)
                .toArray(new QuestionOptionMO[0]);
        StringBuilder answer = new StringBuilder();
        for (int i = 0 ; i < questionMOResponseOptions.length ; i++) {
            if (questionMOResponseOptions[i].optionSelected) {
                for (int j = 0 ; j < questionOptionMOs.length ; j++) {
                    if (questionOptionMOs[j].id.equals(questionMOResponseOptions[i].questionOptionMOId)) {
                        answer.append(questionOptionMOs[j].answerText);
                    }
                }
                answer.append("\n");
            }
        }
        return answer.toString();
    }

    @Override
    public Class<? extends AnswerQuestionActivity> getAnswerQuestionClass() {
        return AnswerQuestionMO.class;
    }

    @Override
    public Class<? extends AppCompatActivity> getViewQuestionResponseClass() {
        return ViewQuestionMOResponse.class;
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
        this.questionMOId = question.getId();
        QuestionMO questionMO = (QuestionMO) question;
        this.questionOptionMOs = questionMO.questionOptionMOs;
        this.questionMOResponseOptions =
                new QuestionMOResponseOption[questionMO.questionOptionMOs.length];
        for (int i = 0 ; i < questionMO.questionOptionMOs.length ; i++) {
            this.questionMOResponseOptions[i] = new QuestionMOResponseOption();
            this.questionMOResponseOptions[i].questionMOResponseId = this.id;
            this.questionMOResponseOptions[i].questionOptionMOId = questionOptionMOs[i].id;
            this.questionMOResponseOptions[i].optionSelected = false;
        }
    }

    @Override
    public void insertToDb(AppDatabase db) {
        db.questionMOResponseDAO().insert(this);
        for (QuestionMOResponseOption questionMOResponseOption : this.questionMOResponseOptions) {
            db.questionMOResponseOptionDAO().insert(questionMOResponseOption);
        }
    }
}
