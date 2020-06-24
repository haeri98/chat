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
		response.getWriter().print("<script>alert('�α׾ƿ��Ǿ����ϴ�.');"
				+ "location.href='login';</script>");
	}

	@RequestMapping(value = "/loginProcess", method = RequestMethod.POST)
	public ModelAndView loginProcess(String id, String password, HttpServletRequest request,
			HttpServletResponse response, HttpSession session, ModelAndView mv) throws Exception {
		Map<String, Object> map = chatService.isId(id, password);
		int result = Integer.parseInt(map.get("result").toString());
		logger.info("result = "+result);

		if (result == 1) { // �α��� ����
			for(Cart cart : EchoHandler.sessionList) {
				if(cart.getId().equals(id)) {
					response.setContentType("text/html;charset=utf-8");
					response.getWriter().print("<script>alert('�̹� ���Ͽ� ���� �� �Դϴ�.');"
							+ "history.back();</script>");
					return null;
				}
			}
			session.setAttribute("id", id);
			mv.setViewName("_2/boot");
			mv.addObject("name",id);
			mv.addObject("filename", map.get("filename").toString());
			// mv.addObject("filename", map.get("original").toString());
			
			// ip�� �����ϴ� ���� localhost �����ϴ� ��� ��� �����ϱ� ���� ������ url�� ���մϴ�.
			String requestURL = request.getRequestURL().toString();
			System.out.println(requestURL);
			int start = requestURL.indexOf("//");
			int end = requestURL.lastIndexOf("/");
			String url = requestURL.substring(start,end);
			System.out.println("url = "+url);
			mv.addObject("url", url);
			return mv;
		} else {
			String message = "��й�ȣ�� ��ġ���� �ʽ��ϴ�.";
			if (result == -1)
				message = "���̵� �������� �ʽ��ϴ�.";
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
			String fileName = uploadfile.getOriginalFilename(); // ���� ���ϸ�
			chat.setOriginalfile(fileName); // ���� ���ϸ� ����

			// ���ο� ���� �̸� : ���� ��-��-��
			Calendar c = Calendar.getInstance();
			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH) + 1;
			int date = c.get(Calendar.DATE);
			//String saveFolder = request.getSession().getServletContext().getRealPath("resources") + "/upload/";
			String homedir = saveFolder + year + "-" + month + "-" + date;
			System.out.println(homedir);
			File path1 = new File(homedir);
			if (!(path1.exists())) {
				path1.mkdir(); // ���ο� ���� ����
			}

			// ���� �߻�
			Random r = new Random();
			int random = r.nextInt(100000000);

			/** Ȯ���� ���ϱ� ���� **/
			int index = fileName.lastIndexOf(".");
			/*
			 * ���ڿ����� Ư�����ڿ��� ��ġ ��(index)�� ��ȯ�Ѵ�. indexOf�� ó�� �߰ߵǴ� ���ڿ��� ���� index�� ��ȯ�ϴ� �ݸ�,
			 * lastIndexOf�� ���������� �߰ߵǴ� ���ڿ��� index�� ��ȯ�մϴ�. (���ϸ� ���� ������ ���� ��� �� �������� �߰ߵǴ�
			 * ���ڿ��� ��ġ�� �����մϴ�.)
			 */
			System.out.println("index = " + index);

			String fileExtension = fileName.substring(index + 1);
			System.out.println("fileExtension = " + fileExtension);
			/** Ȯ���� ���ϱ� �� **/

			// ���ο� ���ϸ�
			String refileName = "bbs" + year + month + date + random + "." + fileExtension;
			System.out.println("refileName = " + refileName);

			// ����Ŭ db�� ����� ���ϸ�
			String fileDBName = "/" + year + "-" + month + "-" + date + "/" + refileName;
			System.out.println("fileDBName = " + fileDBName);

			// ���ε��� ������ �Ű������� ��ο� ����
			uploadfile.transferTo(new File(saveFolder + fileDBName));

			// �ٲ� ���ϸ����� ����
			chat.setSavefile(fileDBName);
		}
		
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();

		int result = chatService.insert(chat);
		out.println("<script>");
		// ������ �� ���
		if (result == 1) {
			out.println("alert('ȸ�������� �����մϴ�.');");
			out.println("location.href='login';");
		} else if (result == -1) {
			out.println("alert('���̵� �ߺ��Ǿ����ϴ�. �ٽ� �Է��ϼ���');");
			out.println("history.back();");
		} else {
			out.println("alert('ȸ�� ���Կ� �����߽��ϴ�.');");
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
