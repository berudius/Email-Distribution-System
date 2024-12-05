package com.InternShip.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.InternShip.Models.Log;
import com.InternShip.Models.Non_Saved_In_DB.UserLogStats;
import com.InternShip.Repository.LogRepository;

@Service
public class LogService {

    @Autowired
    LogRepository logRepository;
    int maxPageCapacity = 10;

    
    
   public Page<UserLogStats> getUserLogStats(int page){
    PageRequest request = PageRequest.of(page, maxPageCapacity);
    Page<UserLogStats> logs = logRepository.getUserLogStats(request);
    return logs;
   }


   public Log saveLog(Log log){
    return logRepository.save(log);
   }


   
}
