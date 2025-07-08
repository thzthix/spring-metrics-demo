package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.AccessLevel;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(nullable = false, length = 100)
    private String name;
    
    @Column(nullable = false)
    private Integer age;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private JobType job;
    
    @Column(length = 200)
    private String specialty;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    public static User createUser(String name, Integer age, JobType job, String specialty) {
        return new User(null, name, age, job, specialty, null);
    }
    
    public void updateUser(String name, Integer age, JobType job, String specialty) {
        this.name = name;
        this.age = age;
        this.job = job;
        this.specialty = specialty;
    }
}
