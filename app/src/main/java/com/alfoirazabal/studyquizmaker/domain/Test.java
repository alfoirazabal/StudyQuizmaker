package com.alfoirazabal.studyquizmaker.domain;

import android.content.Context;
import android.content.res.Resources;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.alfoirazabal.studyquizmaker.R;

import java.util.UUID;

@Entity(
        foreignKeys = {
                @ForeignKey(
                        entity = Topic.class,
                        parentColumns = "id",
                        childColumns = "topicId",
                        onDelete = ForeignKey.CASCADE,
                        onUpdate = ForeignKey.CASCADE
                )
        },
        indices = {
                @Index(value = {"topicId"})
        }
)
public class Test {

    @PrimaryKey
    @NonNull
    public String id;

    @ColumnInfo(name = "topicId")
    public String topicId;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "description")
    public String description;

    public Test() {
        this.id = UUID.randomUUID().toString();
    }

    public String toString(Context context) {
        Resources resources = context.getResources();
        return resources.getString(R.string.name) + ": "+ this.name;
    }

}
