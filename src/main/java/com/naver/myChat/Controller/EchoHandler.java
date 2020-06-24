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
		logger.info("웹소켓(서버) 객체생성");
	}

	// @OnOpen 는 클라이언트가 웹소켓에 들어오고 서버에 아무런 문제 없이 들어왔을 때 실행하는 메소드
	// javax.websocket.Session: 접속자마다 한개의 세션이 생성되어 데이터 통신수단으로 사용되며 접속자 마다 구분
	@OnOpen
	public void onOpen(Session session) {
		logger.info("Open session id : " + session.getId());
		logger.info("session 쿼리 스트링 : " + session.getQueryString());

		String queryString = session.getQueryString();
		String id = queryString.substring(queryString.indexOf("=") + 1, queryString.indexOf("&"));
		String filename = queryString.substring(queryString.indexOf("/"));
		Cart cart = new Cart();
		cart.setSession(session);
		cart.setFilename(filename);
		cart.setId(id);
		sessionList.add(cart);

		String message = id + "님이 입장하셨습니다.in";
		sendAllSessionToMessage(session, message);
	}

	// 보낸 사람의 정보(id와 파일이름) 구하기
	private String getinfo(Session self) {
		String infomation = "";
		synchronized (sessionList) {
			for (Cart cart : EchoHandler.sessionList) {
				Session s = cart.getSession();
				if (self.getId().equals(s.getId())) {
					infomation = cart.getId() + "&" + cart.getFilename();
					// 보낸 사람의 정보
					logger.info("보낸 사람의 정보 = " + infomation);
					break;
				}
			}
		}
		return infomation;
	}

	// 나를 제외한 모든 사용자에게 메시지 전달
	private void sendAllSessionToMessage(Session self, String message) {
		String info = getinfo(self);
		synchronized (sessionList) {
			try {
				for (Cart cart : EchoHandler.sessionList) {
					Session s = cart.getSession();
					if (!self.getId().equals(s.getId())) {
						logger.info("나를 제외한 모든 사람에게 보내는 메시지 : " + info + "&" + message);
						s.getBasicRemote().sendText(info+"&"+message);
					}
				}
			} catch (Exception e) {
				logger.info("sendAllSessionToMessage 오류 "+e.getMessage());
			}
		}
	}

	@OnMessage	//메시지를 작성한 후 SEND했을 때, 이 메소드를 실행시키는 Annotation
	public void onMessage2(String message, Session session) {
		logger.info("Message : " + message);
		sendAllSessionToMessage(session, message);
	}

	@OnError
	public void OnError(Throwable e, Session session) {
		logger.info("error 입니다.");
	}

	// 클라이언트와 웹소켓과의 연결이 끊기면 실행되는 메소드
	@OnClose
	public void onClose(Session session) throws InvocationTargetException {
		logger.info("Session " + session.getId() + " has ended");
		remove(session);
	}
	
	private void remove(Session session) {
		synchronized (sessionList) {
			for(int i=0; i<sessionList.size(); i++) {
				Session s = sessionList.get(i).getSession();
				if(session.getId().equals(s.getId())) { // 나와 세션 아이디가 같은 cart를 제거
					sessionList.remove(i);
				}
			}
		}
	}

}
