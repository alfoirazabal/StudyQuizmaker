package com.alfoirazabal.studyquizmaker.db.dao.testrun;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.alfoirazabal.studyquizmaker.domain.testrun.TestRun;

import java.util.List;

@Dao
public interface TestRunDAO {

    @Query("SELECT * FROM testrun WHERE testId = :testId")
    List<TestRun> getRunsFromTest(String testId);

    @Insert
    void insert(TestRun testRun);

    @Delete
    void delete(TestRun testRun);

}
