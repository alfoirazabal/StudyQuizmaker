package com.alfoirazabal.studyquizmaker.db.dao.testrun;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.alfoirazabal.studyquizmaker.domain.testrun.QuestionMOResponseOption;

@Dao
public interface QuestionMOResponseOptionDAO {

    @Query("SELECT * FROM QuestionMOResponseOption WHERE questionMOResponseId = :questionMOResponseId")
    QuestionMOResponseOption[] getByQuestionMOResponse(String questionMOResponseId);

    @Insert
    void insert(QuestionMOResponseOption getByQuestionMOResponse);

    @Delete
    void delete(QuestionMOResponseOption getByQuestionMOResponse);

}
