package com.InternShip.Models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "crons")
public class Cron {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @Column(nullable = false)
    String expression;

    @Column(nullable = false, updatable = false)
    LocalDateTime createdOn;

    @PrePersist
    public void setCreatedOnValue() {
        this.createdOn = LocalDateTime.now();
    }


    public String getFormattedCreatedOn() {
        
        return createdOn != null ? createdOn.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")) : "";
    }

}
