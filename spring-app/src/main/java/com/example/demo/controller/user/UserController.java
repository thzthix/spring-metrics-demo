package com.example.demo.controller.user;

import com.example.demo.controller.user.dto.UserCreateRequestDto;
import com.example.demo.controller.user.dto.UserResponseDto;
import com.example.demo.controller.user.dto.UserUpdateRequestDto;
import com.example.demo.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService service;

    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<UserResponseDto> user(@PathVariable Integer id) {
        UserResponseDto retrieved = service.findById(id);
        return ResponseEntity.ok(retrieved);
    }

    @RequestMapping(path = "", method = RequestMethod.GET)
    public ResponseEntity<List<UserResponseDto>> user() {
        List<UserResponseDto> retrieved = service.findAll();
        return ResponseEntity.ok(retrieved);
    }

    @RequestMapping(path = "", method = RequestMethod.POST)
    public ResponseEntity<UserResponseDto> createUser(@RequestBody @Valid UserCreateRequestDto request) {
        UserResponseDto created = service.save(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(created);
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable Integer id, @RequestBody @Valid UserUpdateRequestDto request) {
        UserResponseDto updated = service.update(id, request);
        return ResponseEntity.ok(updated);
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
