package com.study.springboot.controller;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.study.springboot.entity.User;
import com.study.springboot.repository.UserRepository;
import com.study.springboot.service.ExerciseLogService; // ✅ 추가

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

    private final UserRepository userRepository;
    private final ExerciseLogService exerciseLogService; // ✅ 서비스 필드 추가

    public UserController(UserRepository userRepository, ExerciseLogService exerciseLogService) {
        this.userRepository = userRepository;
        this.exerciseLogService = exerciseLogService; // ✅ 생성자에서 주입
    }

    @GetMapping
    public List<User> getAllUsers() {
        return StreamSupport.stream(userRepository.findAll().spliterator(), false)
                            .collect(Collectors.toList());
    }


    @PostMapping
    public User createUser(@RequestBody User user) {
        return userRepository.save(user);
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable("id") String id) {
        return userRepository.findById(id).orElse(null);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable("id") String id, @RequestBody User user) {
        User existingUser = userRepository.findById(id).orElse(null);
        if (existingUser != null) {
            if (user.getHeight() != null) existingUser.setHeight(user.getHeight());
            if (user.getWeight() != null) existingUser.setWeight(user.getWeight());
            if (user.getGoalWeight() != null) existingUser.setGoalWeight(user.getGoalWeight());

            userRepository.save(existingUser);
            return ResponseEntity.ok(existingUser);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    // ✅ 오늘 소모한 칼로리 조회
    @GetMapping("/{id}/burned-calories")
    public ResponseEntity<Integer> getTodayBurnedCalories(@PathVariable("id") String id) {
        int todayCalories = exerciseLogService.getTodayCalories(id);
        return ResponseEntity.ok(todayCalories);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable("id") String id) {
        userRepository.deleteById(id);
    }
}
