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
                        entity = Subject.class,
                        parentColumns = "id",
                        childColumns = "subjectId",
                        onDelete = ForeignKey.CASCADE,
                        onUpdate = ForeignKey.CASCADE
                )
        },
        indices = {
                @Index(value = {"subjectId"})
        }
)
public class Topic implements Comparable<Topic> {

    @PrimaryKey
    @NonNull
    public String id;

    @ColumnInfo(name = "subjectId")
    @NonNull
    public String subjectId;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "description")
    public String description;

    public Topic() {
        this.id = UUID.randomUUID().toString();
    }

    public String toString(Context context) {
        Resources resources = context.getResources();
        return resources.getString(R.string.name) + ": " + this.name;
    }

    @Override
    public int compareTo(Topic o) {
        return this.name.toLowerCase().compareTo(o.name.toLowerCase());
    }
}
