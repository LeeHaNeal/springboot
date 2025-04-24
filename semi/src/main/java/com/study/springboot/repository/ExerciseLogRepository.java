package com.study.springboot.repository;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.study.springboot.entity.ExerciseLog;

public interface ExerciseLogRepository extends JpaRepository<ExerciseLog, Long> {

    @Query(
        value = "SELECT SUM(e.CALORIES_BURNED) FROM EXERCISE_LOGS e WHERE e.USER_ID = :userId AND TRUNC(e.LOG_DATE) = TRUNC(:date)",
        nativeQuery = true
    )
    Integer sumTodayCaloriesByUserId(@Param("userId") String userId, @Param("date") LocalDate date);
}
