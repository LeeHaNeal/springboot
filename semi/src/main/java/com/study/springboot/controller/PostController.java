package com.study.springboot.controller;

import com.study.springboot.dto.CommentDTO;
import com.study.springboot.dto.PostDTO;
import com.study.springboot.entity.Comment;
import com.study.springboot.entity.Post;
import com.study.springboot.entity.User;
import com.study.springboot.repository.PostRepository;
import com.study.springboot.repository.UserRepository;
import com.study.springboot.service.CommentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/posts")
@CrossOrigin(origins = "http://localhost:3000")
public class PostController {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentService commentService;

    // 게시글 작성
    @PostMapping
    public ResponseEntity<PostDTO> createPost(@RequestBody Post post) {
        try {
            User user = userRepository.findById(post.getUser().getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            post.setUser(user);
            Post savedPost = postRepository.save(post);

            PostDTO dto = new PostDTO();
            dto.setId(savedPost.getId());
            dto.setTitle(savedPost.getTitle());
            dto.setContent(savedPost.getContent());
            dto.setUserName(user.getName());
            dto.setUserId(user.getUserId());

            return new ResponseEntity<>(dto, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 게시글 전체 조회
    @GetMapping
    public ResponseEntity<List<PostDTO>> getAllPosts() {
        try {
            List<Post> posts = postRepository.findAll();
            if (posts.isEmpty()) return new ResponseEntity<>(HttpStatus.NO_CONTENT);

            List<PostDTO> dtos = posts.stream()
                    .map(post -> {
                        PostDTO dto = new PostDTO();
                        dto.setId(post.getId());
                        dto.setTitle(post.getTitle());
                        dto.setContent(post.getContent());
                        dto.setUserName(post.getUser().getName());
                        dto.setUserId(post.getUser().getUserId());
                        return dto;
                    })
                    .collect(Collectors.toList());

            return new ResponseEntity<>(dtos, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 게시글 상세 조회
    @GetMapping("/{id}")
    public ResponseEntity<PostDTO> getPostById(@PathVariable("id") Long id) {
        try {
            Post post = postRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Post not found"));

            PostDTO dto = new PostDTO();
            dto.setId(post.getId());
            dto.setTitle(post.getTitle());
            dto.setContent(post.getContent());
            dto.setUserName(post.getUser().getName());
            dto.setUserId(post.getUser().getUserId());

            return new ResponseEntity<>(dto, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

 // 댓글 등록
    @PostMapping("/{postId}/comments")
    public ResponseEntity<?> createComment(
            @PathVariable("postId") Long postId,
            @RequestBody CommentDTO commentDTO) {
        try {
            commentService.saveComment(postId, commentDTO);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("댓글 등록 실패", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 댓글 목록 조회
    @GetMapping("/{postId}/comments")
    public ResponseEntity<List<CommentDTO>> getCommentsByPostId(@PathVariable("postId") Long postId) {
        try {
            List<Comment> comments = commentService.getCommentsByPostId(postId);

            List<CommentDTO> dtos = comments.stream().map(comment -> {
                CommentDTO dto = new CommentDTO();
                dto.setId(comment.getId()); // 🔥 필수: 댓글 ID 내려주기
                dto.setUserId(comment.getUser().getUserId()); // String
                dto.setContent(comment.getContent());
                dto.setCreatedAt(comment.getCreatedAt());
                return dto;
            }).collect(Collectors.toList());

            return new ResponseEntity<>(dtos, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 댓글 삭제
    @PostMapping("/{postId}/comments/{commentId}/delete")
    public ResponseEntity<?> deleteComment(
            @PathVariable("postId") Long postId,
            @PathVariable("commentId") Long commentId,
            @RequestBody CommentDTO dto) {
        try {
            commentService.deleteComment(commentId, dto.getUserId());
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("삭제 권한 없음");
        }
    }

    // 댓글 수정
    @PostMapping("/{postId}/comments/{commentId}/edit")
    public ResponseEntity<?> updateComment(
            @PathVariable("postId") Long postId,
            @PathVariable("commentId") Long commentId,
            @RequestBody CommentDTO dto) {
        try {
            commentService.updateComment(commentId, dto.getUserId(), dto.getContent());
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("수정 권한 없음");
        }
    }
    
 // 게시글 수정
    @PutMapping("/{id}")
    public ResponseEntity<?> updatePost(@PathVariable("id") Long id, @RequestBody PostDTO postDTO) {
        try {
            Post post = postRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Post not found"));

            // ✅ 작성자 확인: 로그인한 유저와 일치해야 수정 가능
            if (!post.getUser().getUserId().equals(postDTO.getUserId())) {
                return new ResponseEntity<>("수정 권한 없음", HttpStatus.FORBIDDEN);
            }

            post.setTitle(postDTO.getTitle());
            post.setContent(postDTO.getContent());

            postRepository.save(post);

            return new ResponseEntity<>("수정 완료", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("게시글 수정 실패", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
 // 게시글 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePost(@PathVariable("id") Long id) {
        try {
            // 1. 먼저 댓글 삭제
            List<Comment> comments = commentService.getCommentsByPostId(id);
            for (Comment comment : comments) {
                commentService.deleteComment(comment.getId(), comment.getUser().getUserId());
            }

            // 2. 게시글 삭제
            postRepository.deleteById(id);

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("게시글 삭제 실패");
        }
    }

}
