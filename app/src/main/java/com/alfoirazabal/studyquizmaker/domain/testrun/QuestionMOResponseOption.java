package com.alfoirazabal.studyquizmaker.domain.testrun;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.UUID;

@Entity(
        foreignKeys = {
                @ForeignKey(
                        entity = QuestionMOResponse.class,
                        parentColumns = "id",
                        childColumns = "questionMOResponseId",
                        onDelete = ForeignKey.CASCADE,
                        onUpdate = ForeignKey.CASCADE
                )
        },
        indices = {
                @Index(value = {"questionMOResponseId"})
        }
)
public class QuestionMOResponseOption implements Serializable {

    @PrimaryKey
    @NonNull
    public String id;

    @ColumnInfo(name = "questionMOResponseId")
    public String questionMOResponseId;

    @ColumnInfo(name = "optionSelected")
    public boolean optionSelected;

    @ColumnInfo(name = "optionScore")
    public double optionScore;

    public QuestionMOResponseOption() {
        this.id = UUID.randomUUID().toString();
    }

}
