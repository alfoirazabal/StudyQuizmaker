package com.alfoirazabal.studyquizmaker.domain;

import android.content.Context;
import android.content.res.Resources;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.alfoirazabal.studyquizmaker.R;

import java.util.UUID;

@Entity
public class Subject {

    @PrimaryKey
    @NonNull
    public String id;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "description")
    public String description;

    public Subject() {
        this.id = UUID.randomUUID().toString();
    }

    @NonNull
    public String toString(Context context) {
        Resources resources = context.getResources();
        return resources.getString(R.string.name) + ": " + this.name;
    }
}
