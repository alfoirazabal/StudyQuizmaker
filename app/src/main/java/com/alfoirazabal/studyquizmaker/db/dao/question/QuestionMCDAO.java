package com.alfoirazabal.studyquizmaker.db.dao.question;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.alfoirazabal.studyquizmaker.domain.question.QuestionMC;

import java.util.List;

@Dao
public interface QuestionMCDAO {

    @Query("SELECT * FROM questionMC")
    List<QuestionMC> getAll();

    @Query("SELECT * FROM questionMC WHERE id = :questionMCId")
    QuestionMC getById(String questionMCId);

    @Query("SELECT * FROM questionMC WHERE testId = :testId")
    List<QuestionMC> getFromTest(String testId);

    @Insert
    void insert(QuestionMC questionMC);

    @Update
    void update(QuestionMC questionMC);

    @Delete
    void delete(QuestionMC questionMC);

}
