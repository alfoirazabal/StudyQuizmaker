package com.alfoirazabal.studyquizmaker.domain.question;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.alfoirazabal.studyquizmaker.R;
import com.alfoirazabal.studyquizmaker.domain.Test;

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
public class QuestionSimple {

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

    public QuestionSimple() {
        this.id = UUID.randomUUID().toString();
    }

    public String toString(Context context) {
        return context.getString(R.string.title) + ": " + this.title;
    }

}