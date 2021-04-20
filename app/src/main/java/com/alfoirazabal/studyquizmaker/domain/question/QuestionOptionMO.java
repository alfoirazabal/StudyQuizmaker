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
                        entity = QuestionMO.class,
                        parentColumns = "id",
                        childColumns = "questionMOId",
                        onDelete = ForeignKey.CASCADE,
                        onUpdate = ForeignKey.CASCADE
                )
        },
        indices = {
                @Index(value = "questionMOId")
        }
)
public class QuestionOptionMO implements Serializable, Comparable<QuestionOptionMO> {

    @PrimaryKey
    @NonNull
    public String id;

    @ColumnInfo(name = "questionMOId")
    public String questionMOId;

    @ColumnInfo(name = "answerText")
    public String answerText;

    @ColumnInfo(name = "score")
    public double score;

    public QuestionOptionMO() {
        this.id = IdGenerator.generateId();
    }

    @Override
    public int compareTo(QuestionOptionMO o) {
        return Double.compare(o.score, this.score);
    }
}
