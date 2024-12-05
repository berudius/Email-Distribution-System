package com.InternShip.Models;

import java.time.LocalDateTime;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.InternShip.Models.Non_Saved_In_DB.LogType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Builder;

@Entity
@Table(name = "logs")
@Builder
public class Log {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false,  foreignKey = @ForeignKey(name = "fk_log_user"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    LogType type;

    @Column(name = "created_on", nullable = false, updatable = false)
    LocalDateTime createdOn;

    @PrePersist
    public void setCreatedOnValue(){
        this.createdOn = LocalDateTime.now();
    }
}


