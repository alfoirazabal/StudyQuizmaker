package com.alfoirazabal.studyquizmaker.db.dao.question;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.alfoirazabal.studyquizmaker.domain.question.QuestionMO;

import java.util.List;

@Dao
public interface QuestionMODAO {

    @Query("SELECT * FROM QuestionMO")
    List<QuestionMO> getAll();

    @Query("SELECT * FROM QuestionMO WHERE id = :questionMOId")
    QuestionMO getById(String questionMOId);

    @Query("SELECT * FROM QuestionMO WHERE testId = :testId")
    List<QuestionMO> getFromTest(String testId);

    @Query("SELECT COUNT(*) FROM QuestionMO WHERE testId = :testId")
    int getCountFromTest(String testId);

    @Query("SELECT title FROM QuestionMO WHERE testId = :testId")
    List<String> getAllTitles(String testId);

    @Insert
    void insert(QuestionMO questionMO);

    @Update
    void update(QuestionMO questionMO);

    @Delete
    void delete(QuestionMO questionMO);
}
