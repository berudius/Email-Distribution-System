package com.InternShip.Services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

import com.InternShip.Models.Cron;
import com.InternShip.Models.Log;
import com.InternShip.Models.User;
import com.InternShip.Models.Non_Saved_In_DB.LogType;
import com.InternShip.Repository.CronRepository;
import com.InternShip.Repository.UserRepository;

import jakarta.annotation.PostConstruct;

@EnableScheduling
@Service
public class CronJobScheduler {
    @Autowired
    private CronRepository cronRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private LogService logService;
    @Autowired
    ThreadPoolTaskScheduler taskSheduler;

    //при запуску програми здійснити планування виконання операції надсилання повідомлення відповідно до cron job expression
    @PostConstruct
    public void init(){
         startScheduledJobs();
    }
    
    public void startScheduledJobs(){
        List<User> users = userRepository.findAll();
        List<Cron> cronJobs = cronRepository.findAll();
        for (Cron cron : cronJobs) {
            for (User user : users) {
                scheduleCronJob(user, cron.getExpression());
            }
        }
    }

    private void scheduleCronJob(User user, String cronExpression){
        taskSheduler.schedule( () -> {

           String messageText = "Ім'я користувача: " + user.getName() + "\nДата та час створення: " + user.getFormattedCreatedOn();            
           boolean isMessageSended = emailService.sendEmail(user.getEmail(), "Вітання", messageText);
           
           if(isMessageSended){
            Log log = Log.builder().user(user).type(LogType.CRON).build();
            logService.saveLog(log);
           }
           
        }, new CronTrigger(cronExpression));
    }
}
