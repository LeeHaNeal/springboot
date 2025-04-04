package com.study.springboot.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.study.springboot.domain.Board;
import com.study.springboot.service.BoardRestService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/rest")
@RequiredArgsConstructor
public class BoardRestController {

    private final BoardRestService boardRestService;

    // 1. 모든 board 레코드 가져오기
    @GetMapping("/boardall")
    public List<Board> getAllBoards() {
        return boardRestService.getAllBoards();
    }

    // 2. 제목과 내용을 기준으로 검색해서 가져오기
    @GetMapping("/boards")
    public List<Board> searchBoards(
            @RequestParam(name = "title", required = false) String title,
            @RequestParam(name = "content", required = false) String content) {
        return boardRestService.searchBoards(title, content);
    }

 // 3. 특정 id의 board 가져오기
    @GetMapping("/boardone/{id}")
    public ResponseEntity<Board> getBoardById(@PathVariable("id") Long id) {
        Board board = boardRestService.getBoardById(id);
        if (board != null) {
            return ResponseEntity.ok(board);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // 4. 특정 id의 board 삭제하기
    @DeleteMapping("/boarddel/{id}")
    public ResponseEntity<Void> deleteBoard(@PathVariable("id") Long id) {
        boolean deleted = boardRestService.deleteBoard(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
