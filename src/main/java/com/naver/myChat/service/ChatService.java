package com.naver.myChat.service;

import java.util.Map;

import com.naver.myChat.domain.Chat;

public interface ChatService {
	public Map<String, Object> isId(String id, String password);
	public int insert(Chat m);
	public int isId(String id);
}
