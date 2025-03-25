package com.study.springboot.multi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Person {
	@Value("더조은")
	private String name;
	@Value("학원")
	private String nickname;
	@Autowired //자동으로 객체를 찾아서 bean으로 등록해줌 , 하나일때는 자동으로 등록
	@Qualifier("printerA") // 1개 이상일 때는 명시해 줘야됨
	private Printer printer;
	public Person() {
	}
	
	public Person(String name, String nickname, Printer printer) {
		super();
		this.name = name;
		this.nickname = nickname;
		this.printer = printer;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public Printer getPrinter() {
		return printer;
	}

	public void setPrinter(Printer printer) {
		this.printer = printer;
	}
	
	public void print() {
		printer.print(" Hello " + name + " : " + nickname);
	}
	
	
	
	
}
