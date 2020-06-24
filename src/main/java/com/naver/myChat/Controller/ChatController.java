package com.naver.myChat.Controller;

import java.io.File;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.naver.myChat.domain.Cart;
import com.naver.myChat.domain.Chat;
import com.naver.myChat.service.ChatService;

@Controller
public class ChatController {

	@Autowired
	private ChatService chatService;
	
	@Value("${savefoldername}")
	private String saveFolder;
	
	private static final Logger logger = LoggerFactory.getLogger(ChatController.class);

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login() {
		return "_2/loginForm";
	}
	
	@RequestMapping(value="/logout")
	public void logout(HttpSession session, HttpServletResponse response) throws Exception {
		session.invalidate();
		response.setContentType("text/html;charset=utf-8");
		response.getWriter().print("<script>alert('로그아웃되었습니다.');"
				+ "location.href='login';</script>");
	}

	@RequestMapping(value = "/loginProcess", method = RequestMethod.POST)
	public ModelAndView loginProcess(String id, String password, HttpServletRequest request,
			HttpServletResponse response, HttpSession session, ModelAndView mv) throws Exception {
		Map<String, Object> map = chatService.isId(id, password);
		int result = Integer.parseInt(map.get("result").toString());
		logger.info("result = "+result);

		if (result == 1) { // 로그인 성공
			for(Cart cart : EchoHandler.sessionList) {
				if(cart.getId().equals(id)) {
					response.setContentType("text/html;charset=utf-8");
					response.getWriter().print("<script>alert('이미 소켓에 연결 중 입니다.');"
							+ "history.back();</script>");
					return null;
				}
			}
			session.setAttribute("id", id);
			mv.setViewName("_2/boot");
			mv.addObject("name",id);
			mv.addObject("filename", map.get("filename").toString());
			// mv.addObject("filename", map.get("original").toString());
			
			// ip로 접근하는 경우와 localhost 접근하는 경우 모두 적용하기 위해 접근할 url을 구합니다.
			String requestURL = request.getRequestURL().toString();
			System.out.println(requestURL);
			int start = requestURL.indexOf("//");
			int end = requestURL.lastIndexOf("/");
			String url = requestURL.substring(start,end);
			System.out.println("url = "+url);
			mv.addObject("url", url);
			return mv;
		} else {
			String message = "비밀번호가 일치하지 않습니다.";
			if (result == -1)
				message = "아이디가 존재하지 않습니다.";
			response.setContentType("text/html;charset=utf-8");
			PrintWriter out = response.getWriter();
			out.println("<script>");
			out.println("alert('" + message + "');");
			out.println("location.href='login';");
			out.println("</script>");
			out.close();
			return null;
		}
	} 

	@RequestMapping(value = "/join", method = RequestMethod.GET)
	public String join() {
		return "_2/joinForm";
	}

	@PostMapping(value = "/joinProcess")
	public void joinProcess(Chat chat, HttpServletResponse response, HttpServletRequest request) throws Exception {
		
		MultipartFile uploadfile = chat.getUploadfile();
		if (!uploadfile.isEmpty()) {
			String fileName = uploadfile.getOriginalFilename(); // 원래 파일명
			chat.setOriginalfile(fileName); // 원래 파일명 저장

			// 새로운 폴더 이름 : 오늘 년-월-일
			Calendar c = Calendar.getInstance();
			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH) + 1;
			int date = c.get(Calendar.DATE);
			//String saveFolder = request.getSession().getServletContext().getRealPath("resources") + "/upload/";
			String homedir = saveFolder + year + "-" + month + "-" + date;
			System.out.println(homedir);
			File path1 = new File(homedir);
			if (!(path1.exists())) {
				path1.mkdir(); // 새로운 폴더 생성
			}

			// 난수 발생
			Random r = new Random();
			int random = r.nextInt(100000000);

			/** 확장자 구하기 시작 **/
			int index = fileName.lastIndexOf(".");
			/*
			 * 문자열에서 특정문자열의 위치 값(index)를 반환한다. indexOf가 처음 발견되는 문자열에 대한 index를 반환하는 반면,
			 * lastIndexOf는 마지막으로 발견되는 문자열의 index를 반환합니다. (파일명에 점이 여러개 있을 경우 맨 마지막에 발견되는
			 * 문자열의 위치를 리턴합니다.)
			 */
			System.out.println("index = " + index);

			String fileExtension = fileName.substring(index + 1);
			System.out.println("fileExtension = " + fileExtension);
			/** 확장자 구하기 끝 **/

			// 새로운 파일명
			String refileName = "bbs" + year + month + date + random + "." + fileExtension;
			System.out.println("refileName = " + refileName);

			// 오라클 db에 저장될 파일명
			String fileDBName = "/" + year + "-" + month + "-" + date + "/" + refileName;
			System.out.println("fileDBName = " + fileDBName);

			// 업로드한 파일을 매개변수의 경로에 저장
			uploadfile.transferTo(new File(saveFolder + fileDBName));

			// 바뀐 파일명으로 저장
			chat.setSavefile(fileDBName);
		}
		
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();

		int result = chatService.insert(chat);
		out.println("<script>");
		// 삽입이 된 경우
		if (result == 1) {
			out.println("alert('회원가입을 축하합니다.');");
			out.println("location.href='login';");
		} else if (result == -1) {
			out.println("alert('아이디가 중복되었습니다. 다시 입력하세요');");
			out.println("history.back();");
		} else {
			out.println("alert('회원 가입에 실패했습니다.');");
			out.println("history.back();");
		}
		out.println("</script>");
		out.close();
	}
	
	@ResponseBody
	@RequestMapping(value = "/idcheck")
	public int idCheck(String id) throws Exception {
		int result = chatService.isId(id);
		return result;
	}
}
