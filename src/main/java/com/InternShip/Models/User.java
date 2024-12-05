package com.InternShip.Models;



import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Builder
@Data
@Entity
@Table (name = "users")
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter (AccessLevel.NONE)
    long id;

    @Column (nullable = false)
    String name;
    @Column (nullable = false, unique = true)
    String email;

    @Column(nullable = false, updatable = false)
    @Setter (AccessLevel.NONE)
    LocalDateTime createdOn;

    @Column(nullable = false)
    LocalDateTime updatedOn;


    @PrePersist
    private void setCreatedOnValue(){
        createdOn = LocalDateTime.now();
        updatedOn = LocalDateTime.now();
    }

    @PreUpdate
    private void setUpdatedon(){
        updatedOn = LocalDateTime.now();
    }
    
    
    
    public String getFormattedCreatedOn() {
        
        return createdOn != null ? createdOn.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")) : "";
    }

    public String getFormattedUpdatedOn() {
        return updatedOn != null ? updatedOn.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")) : "";
    }



}
