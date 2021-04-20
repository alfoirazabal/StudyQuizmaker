package com.alfoirazabal.studyquizmaker.domain.question;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.alfoirazabal.studyquizmaker.helpers.IdGenerator;

import java.io.Serializable;

@Entity(
        foreignKeys = {
                @ForeignKey(
                        entity = QuestionMC.class,
                        parentColumns = "id",
                        childColumns = "questionMCId",
                        onDelete = ForeignKey.CASCADE,
                        onUpdate = ForeignKey.CASCADE
                )
        },
        indices = {
                @Index(value = {"questionMCId"})
        }
)
public class QuestionOptionMC implements Serializable, Comparable<QuestionOptionMC> {

    @PrimaryKey
    @NonNull
    public String id;

    @ColumnInfo(name = "questionMCId")
    public String questionMCId;

    @ColumnInfo(name = "answerText")
    public String answerText;

    @ColumnInfo(name = "score")
    public double score;

    public QuestionOptionMC() {
        this.id = IdGenerator.generateId();
    }

    @Override
    public int compareTo(QuestionOptionMC o) {
        return Double.compare(o.score, this.score);
    }
}
