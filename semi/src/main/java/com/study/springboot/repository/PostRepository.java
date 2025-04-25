package com.study.springboot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.study.springboot.entity.Post;

public interface PostRepository extends JpaRepository<Post, Long> {
	List<Post> findAllByOrderByIdDesc();
}