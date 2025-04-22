package com.study.springboot.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.study.springboot.dto.PostDTO;
import com.study.springboot.entity.Post;
import com.study.springboot.entity.User;
import com.study.springboot.repository.PostRepository;
import com.study.springboot.repository.UserRepository;

@CrossOrigin(origins = "http://localhost:3000") // React 앱이 실행되는 포트 설정
@RestController
@RequestMapping("/posts")  // 게시글 경로 설정
public class PostController {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    // 게시글 작성 (POST 메서드)
    @PostMapping
    public ResponseEntity<PostDTO> createPost(@RequestBody Post post) {
        try {
            // 사용자를 userId로 찾기
            User user = userRepository.findById(post.getUser().getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            post.setUser(user);  // user 객체 설정

            // 게시글 저장
            Post savedPost = postRepository.save(post);  // id 자동 생성

            // User 정보를 포함하여 PostDTO 생성
            PostDTO postDTO = new PostDTO(savedPost.getId(), savedPost.getTitle(), savedPost.getContent(), user.getName());
            return new ResponseEntity<>(postDTO, HttpStatus.CREATED); // 201 상태 코드
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR); // 500 상태 코드
        }
    }

    @GetMapping
    public ResponseEntity<List<PostDTO>> getPosts() {
        try {
            List<Post> posts = postRepository.findAll();
            if (posts.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204 상태 코드
            }

            // Post 엔티티를 PostDTO로 변환
            List<PostDTO> postDTOs = posts.stream()
                    .map(post -> new PostDTO(post.getId(), post.getTitle(), post.getContent(), post.getUser().getName())) // User 이름 추가
                    .collect(Collectors.toList());

            return new ResponseEntity<>(postDTOs, HttpStatus.OK); // 200 상태 코드
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR); // 500 상태 코드
        }
    }

}
