package com.InternShip.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.InternShip.Models.Cron;

public interface CronRepository extends JpaRepository<Cron, Long> {
    
}
