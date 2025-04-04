package com.study.springboot.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.study.springboot.domain.Member;
import com.study.springboot.dto.ResponseDto;
import com.study.springboot.dto.UserDto;
import com.study.springboot.service.MemberRestService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/rest")
public class MemberRestController {

    @Autowired
    MemberRestService memberRestService;

    // http://localhost:8080/rest/test
    @GetMapping("/test")
    public String test() {
        log.info("test");
        return "test입니다";
    }

    // http://localhost:8080/rest/user?id=10
    @GetMapping("/user")
    public String getMember(@RequestParam(value = "id", defaultValue = "1") String id) {
        log.info("id : {}", id);
        return "ok : " + id;
    }

    // http://localhost:8080/rest/user/10
    @GetMapping("/user/{id}")
    public String getId(@PathVariable("id") String id) {
        log.info("id : {}", id);
        return "ok : " + id;
    }

    /*
    // http://localhost:8080/swagger-ui.html
    @PostMapping("/userdto")
    public UserDto saveUserDto(@RequestBody UserDto userDto) {
        return new UserDto(memberRestService.saveUserDto(userDto));
    }
    */

    @PostMapping("/userdto")
    public ResponseDto saveUserDto(@RequestBody UserDto userDto) {
        Member m = memberRestService.saveUserDto(userDto);

        ResponseDto responseDto = new ResponseDto();

        if (m.getId() != null) {
            responseDto.setMessage("ok");
            return responseDto;
        } else {
            responseDto.setMessage("fail");
            return responseDto;
        }
    }

    @GetMapping("/userdto")
    public UserDto getuser(@RequestParam("id") String id) {
        log.info("id : {}", id);
        return memberRestService.getUserById(id);
    }

    @GetMapping("/userdto/{id}")
    public UserDto getUserDto(@PathVariable("id") String id) {
        log.info("id : {}", id);
        return memberRestService.getUserById(id);
    }

    @GetMapping("/userall")
    public List<Member> getUserAll() {
        return memberRestService.getUserAll();
    }

    @GetMapping("/userdtoall")
    public List<UserDto> getUserDtoAll() {
        return memberRestService.getUserDtoAll();
    }
}
