package com.alfoirazabal.studyquizmaker.db.dao.question;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.alfoirazabal.studyquizmaker.domain.question.QuestionOptionMC;

import java.util.List;

@Dao
public interface QuestionOptionMCDAO {

    @Query("SELECT * FROM questionOptionMC")
    List<QuestionOptionMC> getAll();

    @Query("SELECT * FROM questionOptionMC WHERE id = :questionMCId")
    QuestionOptionMC getById(String questionMCId);

    @Query("SELECT * FROM questionOptionMC WHERE questionMCId = :questionMCId")
    List<QuestionOptionMC> getFromQuestionMC(String questionMCId);

    @Insert
    void insert(QuestionOptionMC questionOptionMC);

    @Update
    void update(QuestionOptionMC questionOptionMC);

    @Delete
    void delete(QuestionOptionMC questionOptionMC);

}
