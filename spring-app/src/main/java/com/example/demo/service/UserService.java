package com.example.demo.service;

import com.example.demo.controller.user.dto.UserCreateRequestDto;
import com.example.demo.controller.user.dto.UserResponseDto;
import com.example.demo.controller.user.dto.UserUpdateRequestDto;
import com.example.demo.entity.User;
import com.example.demo.exception.CustomException;
import com.example.demo.exception.ExceptionType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;

    public UserResponseDto findById(Integer id) {
        User retrieved = repository.findById(id)
                .orElseThrow(() -> new CustomException(ExceptionType.USER_NOT_FOUND, "유저가 존재하지 않습니다. id : " + id));
        return UserResponseDto.from(retrieved);
    }

    public List<UserResponseDto> findAll() {
        List<User> retrieved = repository.findAll();
        return retrieved.stream().map(UserResponseDto::from).toList();
    }

    @Transactional
    public UserResponseDto save(UserCreateRequestDto request) {
        User createEntity = request.toCreateEntity();
        User retrieved = repository.save(createEntity);
        return UserResponseDto.from(retrieved);
    }

    @Transactional
    public UserResponseDto update(Integer id, UserUpdateRequestDto request) {
        User user = repository.findById(id)
                .orElseThrow(() -> new CustomException(ExceptionType.USER_NOT_FOUND, "유저가 존재하지 않습니다. id : " + id));
        
        user.updateUser(request.getName(), request.getAge(), request.getJob(), request.getSpecialty());
        return UserResponseDto.from(user);
    }

    @Transactional
    public void delete(Integer id) {
        User user = repository.findById(id)
                .orElseThrow(() -> new CustomException(ExceptionType.USER_NOT_FOUND, "유저가 존재하지 않습니다. id : " + id));
        
        repository.delete(user);
    }
}
