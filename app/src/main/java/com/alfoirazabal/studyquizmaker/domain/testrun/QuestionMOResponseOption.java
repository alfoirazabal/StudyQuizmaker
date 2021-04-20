package com.alfoirazabal.studyquizmaker.domain.testrun;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.alfoirazabal.studyquizmaker.domain.question.QuestionOptionMO;
import com.alfoirazabal.studyquizmaker.helpers.IdGenerator;

import java.io.Serializable;

@Entity(
        foreignKeys = {
                @ForeignKey(
                        entity = QuestionMOResponse.class,
                        parentColumns = "id",
                        childColumns = "questionMOResponseId",
                        onDelete = ForeignKey.CASCADE,
                        onUpdate = ForeignKey.CASCADE
                ),
                @ForeignKey(
                        entity = QuestionOptionMO.class,
                        parentColumns = "id",
                        childColumns = "questionOptionMOId",
                        onDelete = ForeignKey.CASCADE,
                        onUpdate = ForeignKey.CASCADE
                )
        },
        indices = {
                @Index(value = {"questionMOResponseId"}),
                @Index(value = {"questionOptionMOId"})
        }
)
public class QuestionMOResponseOption implements Serializable {

    @PrimaryKey
    @NonNull
    public String id;

    @ColumnInfo(name = "questionMOResponseId")
    public String questionMOResponseId;

    @ColumnInfo(name = "questionOptionMOId")
    public String questionOptionMOId;

    @ColumnInfo(name = "optionSelected")
    public boolean optionSelected;

    public QuestionMOResponseOption() {
        this.id = IdGenerator.generateId();
    }

}
