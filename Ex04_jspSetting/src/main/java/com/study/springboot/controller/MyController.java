package com.study.springboot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MyController {
	@RequestMapping("/")
	public @ResponseBody String root() {
		return "JSP 실행";
	}
	
	@GetMapping("/test1")
	public String test1() {
		return "test1";
	}
	
	@RequestMapping("/test2")
	public String test2() {
		return "sub/test2";
	}
	
	
	
}
