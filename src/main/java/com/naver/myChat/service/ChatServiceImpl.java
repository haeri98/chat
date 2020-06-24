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
		int result=-1; // ���̵� �������� �ʴ� ��� -remember�� null�� ���
		if(chat != null) { // ���̵� �����ϴ� ���
			if(chat.getPassword().equals(password)) { // ���̵� ��� ��ġ
				result = 1;
				map.put("filename",chat.getSavefile());
			}else result=0; // ���̺� ���� ��ι�ȣ ����ġ
		}
		map.put("result",result);
		return map;
	}
	
	// �ߺ� ���̵� �˻�
	@Override
	public int isId(String id) {
		Chat chat = dao.isId(id);
		return (chat == null) ? -1 : 1; //-1: ���̵� �������� �ʴ°�� / 1: �Ƶ� ����
	}
}
