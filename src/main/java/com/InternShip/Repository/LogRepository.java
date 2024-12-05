package com.InternShip.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.InternShip.Models.Log;
import com.InternShip.Models.Non_Saved_In_DB.UserLogStats;

public interface LogRepository extends JpaRepository<Log, Integer> {
    @Query(value = "SELECT new com.InternShip.Models.Non_Saved_In_DB.UserLogStats( " +
    "u.name, " +
    "u.email, " +
    "CAST(MIN(l.createdOn) AS TIMESTAMP), " +
    "CAST(MAX(l.createdOn) AS TIMESTAMP), " +
    "SUM(CASE WHEN l.type = 'REST' THEN 1 ELSE 0 END), " +
    "SUM(CASE WHEN l.type = 'CRON' THEN 1 ELSE 0 END) " +
") " +
"FROM Log l " +
"JOIN l.user u " + 
"GROUP BY u.id, u.name, u.email " +
"ORDER BY SUM(CASE WHEN l.type = 'REST' THEN 1 ELSE 0 END) + SUM(CASE WHEN l.type = 'CRON' THEN 1 ELSE 0 END) DESC "
)
public Page<UserLogStats> getUserLogStats(PageRequest page);



}
