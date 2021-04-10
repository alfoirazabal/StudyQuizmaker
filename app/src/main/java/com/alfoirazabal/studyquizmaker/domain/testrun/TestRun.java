package com.alfoirazabal.studyquizmaker.domain.testrun;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.alfoirazabal.studyquizmaker.domain.Test;

import java.io.Serializable;
import java.util.Date;
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
    public QuestionSimpleResponse[] questionSimpleResponses;

    @Ignore
    public int currentQuestionIndex;

    public TestRun() {
        this.id = UUID.randomUUID().toString();
        this.currentQuestionIndex = 0;
        this.dateTimeStarted = new Date();
    }

    @Override
    public int compareTo(TestRun o) {
        return o.dateTimeStarted.compareTo(this.dateTimeStarted);
    }
}
