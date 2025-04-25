package com.study.springboot.repository;

import org.springframework.data.repository.CrudRepository;

import com.study.springboot.entity.User;

public interface UserRepository extends CrudRepository<User, String> {
}
