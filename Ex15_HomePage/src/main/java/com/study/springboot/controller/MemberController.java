package com.study.springboot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.study.springboot.domain.Member;
import com.study.springboot.service.MemberService;

@Controller
public class MemberController {
	@Autowired
	MemberService memberService;
	
	@Autowired
	PasswordEncoder pEncoder;
	
	@RequestMapping("/")
	public String root() {
		return "index";
	}
	@GetMapping("/enrollForm")
	public String enrollForm() {
		return "member/enrollForm";
	}
	@GetMapping("/idCheck")
	public @ResponseBody boolean idCheck(@RequestParam("id") String id) {
		return memberService.idCheck(id);
	}
	
	@PostMapping("/memberInsert")
	public String insert(Member member) {
		String enPass = pEncoder.encode(member.getPassword());
		member.setPassword(enPass);
		
		return "redirect:/";
	}
	
	/*
	 	로그인시 
	 	password
	 	
	 	pEncode.matches(사용자가 넣은 패스워드 DB에서 가져온 패스워드)
	  
	  
	 */
	
	
    @PostMapping("/login")
    public String login(@RequestParam("id") String id, @RequestParam("password") String password) {
       
        Member member = memberService.findById(id);

        if (member != null && pEncoder.matches(password, member.getPassword())) {
           
            return "redirect:/"; 
        } else {
          
            return "member/loginForm"; 
        }
    }
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
