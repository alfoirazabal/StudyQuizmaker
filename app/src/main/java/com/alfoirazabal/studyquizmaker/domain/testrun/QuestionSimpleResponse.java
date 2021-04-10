package com.alfoirazabal.studyquizmaker.domain.testrun;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.alfoirazabal.studyquizmaker.domain.question.QuestionSimple;

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
public class QuestionSimpleResponse implements Serializable {

    @PrimaryKey
    @NonNull
    public String id;

    @ColumnInfo(name = "testRunId")
    public String testRunId;

    @ColumnInfo(name = "questionSimpleId")
    public String questionSimpleId;

    @ColumnInfo(name = "answered")
    public String answered;

    @ColumnInfo(name = "score")
    public double score;

    @ColumnInfo(name = "isAnswered")
    public boolean isAnswered;

    public QuestionSimpleResponse() {
        this.id = UUID.randomUUID().toString();
        this.answered = "";
        this.score = 0;
    }

}
