package com.alfoirazabal.studyquizmaker.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.alfoirazabal.studyquizmaker.domain.Test;

import java.util.List;

@Dao
public interface TestDAO {

    @Query("SELECT * FROM test")
    List<Test> getAll();

    @Query("SELECT name FROM test WHERE topicId = :topicId")
    List<String> getAllTestNames(String topicId);

    @Query("SELECT * FROM test WHERE id = :testId")
    Test getById(String testId);

    @Insert
    void insert(Test test);

    @Update
    void update(Test test);

    @Delete
    void delete(Test test);

}
