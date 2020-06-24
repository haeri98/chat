package com.naver.myChat.dao;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.naver.myChat.domain.Chat;

@Repository
public class ChatDAO {
	
	@Autowired
	private SqlSessionTemplate sqlSession;
	
	public int insert(Chat m) {
		return sqlSession.insert("Chats.insert", m);
	}
	
	public Chat isId(String id) {
		return sqlSession.selectOne("Chats.idcheck",id);
	}

}
