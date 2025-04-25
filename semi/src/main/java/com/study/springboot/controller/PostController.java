package com.study.springboot.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.study.springboot.dto.CommentDTO;
import com.study.springboot.dto.PostDTO;
import com.study.springboot.entity.Comment;
import com.study.springboot.entity.Post;
import com.study.springboot.entity.User;
import com.study.springboot.repository.PostRepository;
import com.study.springboot.repository.UserRepository;
import com.study.springboot.service.CommentService;

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
    public ResponseEntity<?> createPost(@RequestBody Map<String, String> request) {
        String title = request.get("title");
        String content = request.get("content");
        String userId = request.get("userId");
        String passwordHash = request.get("passwordHash");

        if (title == null || content == null || passwordHash == null || userId == null) {
            return ResponseEntity.badRequest().body("입력값이 누락되었습니다.");
        }

        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("존재하지 않는 사용자입니다.");
        }

        User user = userOpt.get();
        if (!user.getPasswordHash().equals(passwordHash)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("비밀번호가 일치하지 않습니다.");
        }

        Post post = new Post();
        post.setTitle(title);
        post.setContent(content);
        post.setUser(user);

        // ✅ 관리자라면 공지사항 설정
        if ("admin".equals(user.getUserId())) {
            post.setIsNotice(true);
        } else {
            post.setIsNotice(false);
        }

        Post saved = postRepository.save(post);
        return ResponseEntity.ok(saved);
    }


    // 게시글 전체 조회
    @GetMapping
    public ResponseEntity<List<PostDTO>> getAllPosts() {
        try {
        	List<Post> posts = postRepository.findAll().stream()
        		    .sorted((a, b) -> {
        		        // 공지 먼저, 그 안에서는 최신순
        		        if (a.getIsNotice() && !b.getIsNotice()) return -1;
        		        if (!a.getIsNotice() && b.getIsNotice()) return 1;
        		        return b.getId().compareTo(a.getId()); // 최신순 정렬
        		    })
        		    .collect(Collectors.toList());

            if (posts.isEmpty()) return new ResponseEntity<>(HttpStatus.NO_CONTENT);

            List<PostDTO> dtos = posts.stream()
                    .map(post -> {
                        PostDTO dto = new PostDTO();
                        dto.setId(post.getId());
                        dto.setTitle(post.getTitle());
                        dto.setContent(post.getContent());
                        dto.setUserName(post.getUser().getName());
                        dto.setUserId(post.getUser().getUserId());
                        dto.setCreatedAt(post.getCreatedAt()); // ✅ 작성일 추가
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
            dto.setCreatedAt(post.getCreatedAt()); // ✅ 작성일 추가

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
                dto.setId(comment.getId());
                dto.setUserId(comment.getUser().getUserId());
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

            // ✅ 관리자이거나 작성자 본인만 수정 가능
            if (!post.getUser().getUserId().equals(postDTO.getUserId()) 
                    && !postDTO.getUserId().equals("admin")) {
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
    public ResponseEntity<?> deletePost(@PathVariable("id") Long id, @RequestBody Map<String, String> request) {
        String userId = request.get("userId"); // 🔥 프론트에서 전달 필요

        try {
            Post post = postRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Post not found"));

            // ✅ 관리자이거나 작성자 본인만 삭제 가능
            if (!post.getUser().getUserId().equals(userId) && !"admin".equals(userId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("삭제 권한 없음");
            }

            // 댓글 먼저 삭제
            List<Comment> comments = commentService.getCommentsByPostId(id);
            for (Comment comment : comments) {
                commentService.deleteComment(comment.getId(), comment.getUser().getUserId());
            }

            postRepository.deleteById(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("게시글 삭제 실패");
        }
    }
}
