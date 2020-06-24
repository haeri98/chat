package com.naver.myChat.Controller;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import com.naver.myChat.domain.Cart;

@Controller
@ServerEndpoint(value = "/boot.do")
public class EchoHandler {
	public static final List<Cart> sessionList = Collections.synchronizedList(new ArrayList<Cart>());
	private static final Logger logger = LoggerFactory.getLogger(EchoHandler.class);

	public EchoHandler() {
		logger.info("������(����) ��ü����");
	}

	// @OnOpen �� Ŭ���̾�Ʈ�� �����Ͽ� ������ ������ �ƹ��� ���� ���� ������ �� �����ϴ� �޼ҵ�
	// javax.websocket.Session: �����ڸ��� �Ѱ��� ������ �����Ǿ� ������ ��ż������� ���Ǹ� ������ ���� ����
	@OnOpen
	public void onOpen(Session session) {
		logger.info("Open session id : " + session.getId());
		logger.info("session ���� ��Ʈ�� : " + session.getQueryString());

		String queryString = session.getQueryString();
		String id = queryString.substring(queryString.indexOf("=") + 1, queryString.indexOf("&"));
		String filename = queryString.substring(queryString.indexOf("/"));
		Cart cart = new Cart();
		cart.setSession(session);
		cart.setFilename(filename);
		cart.setId(id);
		sessionList.add(cart);

		String message = id + "���� �����ϼ̽��ϴ�.in";
		sendAllSessionToMessage(session, message);
	}

	// ���� ����� ����(id�� �����̸�) ���ϱ�
	private String getinfo(Session self) {
		String infomation = "";
		synchronized (sessionList) {
			for (Cart cart : EchoHandler.sessionList) {
				Session s = cart.getSession();
				if (self.getId().equals(s.getId())) {
					infomation = cart.getId() + "&" + cart.getFilename();
					// ���� ����� ����
					logger.info("���� ����� ���� = " + infomation);
					break;
				}
			}
		}
		return infomation;
	}

	// ���� ������ ��� ����ڿ��� �޽��� ����
	private void sendAllSessionToMessage(Session self, String message) {
		String info = getinfo(self);
		synchronized (sessionList) {
			try {
				for (Cart cart : EchoHandler.sessionList) {
					Session s = cart.getSession();
					if (!self.getId().equals(s.getId())) {
						logger.info("���� ������ ��� ������� ������ �޽��� : " + info + "&" + message);
						s.getBasicRemote().sendText(info+"&"+message);
					}
				}
			} catch (Exception e) {
				logger.info("sendAllSessionToMessage ���� "+e.getMessage());
			}
		}
	}

	@OnMessage	//�޽����� �ۼ��� �� SEND���� ��, �� �޼ҵ带 �����Ű�� Annotation
	public void onMessage2(String message, Session session) {
		logger.info("Message : " + message);
		sendAllSessionToMessage(session, message);
	}

	@OnError
	public void OnError(Throwable e, Session session) {
		logger.info("error �Դϴ�.");
	}

	// Ŭ���̾�Ʈ�� �����ϰ��� ������ ����� ����Ǵ� �޼ҵ�
	@OnClose
	public void onClose(Session session) throws InvocationTargetException {
		logger.info("Session " + session.getId() + " has ended");
		remove(session);
	}
	
	private void remove(Session session) {
		synchronized (sessionList) {
			for(int i=0; i<sessionList.size(); i++) {
				Session s = sessionList.get(i).getSession();
				if(session.getId().equals(s.getId())) { // ���� ���� ���̵� ���� cart�� ����
					sessionList.remove(i);
				}
			}
		}
	}

}
