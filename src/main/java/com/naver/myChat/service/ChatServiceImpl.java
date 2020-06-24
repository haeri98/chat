package com.naver.myChat.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.naver.myChat.dao.ChatDAO;
import com.naver.myChat.domain.Chat;

@Service
public class ChatServiceImpl implements ChatService{
	@Autowired
	private ChatDAO dao;
	
	@Transactional
	@Override
	public int insert(Chat c) {
		return dao.insert(c);
	}
	
	@Override
	public Map<String, Object> isId(String id, String password) {
		Map<String, Object> map = new HashMap<String, Object>();
		Chat chat = dao.isId(id);
		int result=-1; // 아이디가 존재하지 않는 경우 -remember가 null인 경우
		if(chat != null) { // 아이디가 존재하는 경우
			if(chat.getPassword().equals(password)) { // 아이디 비번 일치
				result = 1;
				map.put("filename",chat.getSavefile());
			}else result=0; // 아이비 존재 비민번호 불일치
		}
		map.put("result",result);
		return map;
	}
	
	// 중복 아이디 검사
	@Override
	public int isId(String id) {
		Chat chat = dao.isId(id);
		return (chat == null) ? -1 : 1; //-1: 아이디가 존재하지 않는경우 / 1: 아디가 존재
	}
}
