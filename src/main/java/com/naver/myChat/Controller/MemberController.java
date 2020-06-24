package com.naver.myChat.Controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class MemberController {

	@RequestMapping(value = "/loginForm", method = RequestMethod.GET)
	public String login() {
		return "_1/loginForm";
	}
	
	@PostMapping("/login_ok")
	public String login_ok(Model m, String name, HttpServletRequest request) {
		/* 요청주소가 http://localhost:8088/myChat이면
		 * 웹소켓 주소도 ws://localhost:8088/myChat 로 시작해야 하고
		 * 요청주소가 httpL//ip주소:8088/myChat 이면
		 * 웹 소켓 주소도 ws://ip주소:8088/myChat 로 시작해야 합니다.
		 */
		System.out.println("url="+request.getRequestURL());
		int end = request.getRequestURL().lastIndexOf("/");
		String url = request.getRequestURL().substring(7,end);
		System.out.println("url="+url);
		m.addAttribute("url",url);
		m.addAttribute("name", name);
		System.out.println("name:"+name);
		return "_1/websocket_test1";
	}
}