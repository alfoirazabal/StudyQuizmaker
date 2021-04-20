package com.alfoirazabal.studyquizmaker.domain.testrun;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.alfoirazabal.studyquizmaker.domain.Test;
import com.alfoirazabal.studyquizmaker.helpers.IdGenerator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
public class TestRun implements Serializable, Comparable<TestRun> {

    @PrimaryKey
    @NonNull
    public String id;

    @ColumnInfo(name = "testId")
    public String testId;

    @ColumnInfo(name = "scoredPercentage")
    public double scoredPercentage;

    @ColumnInfo(name = "numberOfAnsweredQuestions")
    public int numberOfAnsweredQuestions;

    @ColumnInfo(name = "numberOfTotalQuestions")
    public int numberOfTotalQuestions;

    @ColumnInfo(name = "dateTimeStarted")
    public Date dateTimeStarted;

    @ColumnInfo(name = "dateTimeFinished")
    public Date dateTimeFinished;

    @Ignore
    public QuestionResponse[] questionResponses;

    @Ignore
    public int currentQuestionIndex;

    public TestRun() {
        this.id = IdGenerator.generateId();
        this.currentQuestionIndex = 0;
        this.dateTimeStarted = new Date();
    }

    @Override
    public int compareTo(TestRun o) {
        return o.dateTimeStarted.compareTo(this.dateTimeStarted);
    }

    public boolean hasSimpleQuestions() {
        boolean hasSimpleQuestionsResponses = false;
        for (int i = 0 ; !hasSimpleQuestionsResponses && i < this.questionResponses.length ; i++) {
            hasSimpleQuestionsResponses =
                    (this.questionResponses[i] instanceof QuestionSimpleResponse);
        }
        return hasSimpleQuestionsResponses;
    }

    public List<QuestionSimpleResponse> getQuestionSimpleResponses() {
        List<QuestionSimpleResponse> questionSimpleResponses = new ArrayList<>();
        for (QuestionResponse questionResponse : this.questionResponses) {
            if (questionResponse instanceof QuestionSimpleResponse) {
                questionSimpleResponses.add((QuestionSimpleResponse) questionResponse);
            }
        }
        return questionSimpleResponses;
    }

}
