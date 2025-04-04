package com.study.springboot.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.study.springboot.domain.Board;
import com.study.springboot.repository.BoardRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardRestService {

    private final BoardRepository boardRepository;

    public List<Board> getAllBoards() {
        return boardRepository.findAll();
    }

    public List<Board> searchBoards(String title, String content) {
        if (title == null) title = "";
        if (content == null) content = "";
        return boardRepository.findByTitleContainingAndContentContaining(title, content);
    }

    public Board getBoardById(Long id) {	
        return boardRepository.findById(id).orElse(null);
    }

    public boolean deleteBoard(Long id) {
        if (boardRepository.existsById(id)) {
            boardRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
