package com.example.demo.controller.user.dto;

import com.example.demo.entity.JobType;
import com.example.demo.entity.User;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserCreateRequestDto {
    @NotBlank(message = "이름은 필수입니다")
    private final String name;
    
    @Min(value = 10, message = "나이는 10살 이상이어야 합니다")
    @NotNull(message = "나이는 필수입니다")
    private final Integer age;
    
    @NotNull(message = "직업은 필수입니다")
    private final JobType job;
    
    private final String specialty;

    public User toCreateEntity() {
        return User.createUser(name, age, job, specialty);
    }
}
