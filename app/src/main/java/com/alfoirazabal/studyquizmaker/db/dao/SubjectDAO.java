package com.alfoirazabal.studyquizmaker.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.alfoirazabal.studyquizmaker.domain.Subject;

import java.util.List;

@Dao
public interface SubjectDAO {

    @Query("SELECT * FROM subject")
    List<Subject> getAll();

    @Query("SELECT name FROM subject")
    List<String> getAllSubjectNames();

    @Update
    void update(Subject subject);

    @Insert
    void insert(Subject subject);

    @Delete
    void delete(Subject subject);

}
