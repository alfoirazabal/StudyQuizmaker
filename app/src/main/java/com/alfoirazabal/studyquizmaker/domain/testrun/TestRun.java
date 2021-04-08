package com.alfoirazabal.studyquizmaker.domain.testrun;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.alfoirazabal.studyquizmaker.domain.Test;
import com.alfoirazabal.studyquizmaker.domain.question.QuestionSimple;

import java.io.Serializable;
import java.util.ArrayList;
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
                @Index(value = {"testId"})
        }
)
public class TestRun implements Serializable {

    @PrimaryKey
    @NonNull
    public String id;

    @ColumnInfo(name = "testId")
    public String testId;

    public transient List<QuestionSimple> questionSimples;
    public transient List<QuestionSimpleResponse> questionSimpleResponses;

    public TestRun() {
        this.id = UUID.randomUUID().toString();
        this.questionSimples = new ArrayList<>();
        this.questionSimpleResponses = new ArrayList<>();
    }

    public void loadQuestionSimples(List<QuestionSimple> questionSimples) {
        this.questionSimples.addAll(questionSimples);
    }

    public void setQuestionAnswered(QuestionSimple questionSimple, double score) {
        questionSimples.remove(questionSimple);
        QuestionSimpleResponse response = new QuestionSimpleResponse();
        response.testRunId = this.id;
        response.questionSimpleId = questionSimple.id;
        response.score = score;
        this.questionSimpleResponses.add(response);
    }

}
