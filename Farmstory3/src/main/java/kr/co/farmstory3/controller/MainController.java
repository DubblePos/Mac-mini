package kr.co.farmstory3.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.co.farmstory3.service.CommonService;
import kr.co.farmstory3.vo.FileVo;



public class MainController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Map<String, Object> instances = new HashMap<>();

	@Override
	public void init(ServletConfig config) throws ServletException {
		// ���� ��û�� ����Ǵ� ��Ʈ�ѷ� �ʱ�ȭ �޼���
		System.out.println("MainController init ����!!!");
		
		// �׼��ּ� ������Ƽ ���� ��� ���ϱ�
		ServletContext ctx = config.getServletContext();
		String path = ctx.getRealPath("/WEB-INF") + "/urlMapping.properties";
		
		// ������Ƽ ���� �Է½�Ʈ�� ���� �� ������Ƽ ��ü ����
		Properties prop = new Properties();
		
		try {
			FileInputStream fis = new FileInputStream(path);
			prop.load(fis);
			fis.close();
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		// ������Ƽ ��ü�� ���� ��ü ����
		Iterator iter = prop.keySet().iterator();
		
		while(iter.hasNext()) {
			
			String k = iter.next().toString();
			String v = prop.getProperty(k);
			
			try {
				
				Class obj = Class.forName(v);
				Object instance = obj.newInstance();
				
				instances.put(k, instance);
				
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
	} // init end...
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		requestProc(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		requestProc(req, resp);
	}
	
	protected void requestProc(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// ��û�ּҿ��� key ���ϱ�
		String path = req.getContextPath();
		String uri  = req.getRequestURI();
		String key = uri.substring(path.length());
		
		// Map���� Service ��ü ������
		CommonService instance = (CommonService) instances.get(key);
		
		// Service ��ü ���� �� ��� ���� �ޱ�
		String result = instance.requestProc(req, resp);
		
		if(result.startsWith("redirect:")) {
			String redirecUrl = result.substring(9);
			resp.sendRedirect(path+redirecUrl);
		}else if(result.startsWith("json:")) {
			PrintWriter out = resp.getWriter();
			out.print(result.substring(5));			
			
		}else if(result.startsWith("file:")) {// 클라이언트로 파일 전송	
			
			// Service에서 저장한 FileVo 객체 Controller에서 꺼내기
			FileVo fvo = (FileVo) req.getAttribute("fvp");
			
			// response 헤더 수정
			resp.setContentType("application/octet-stream");
			resp.setHeader("Content-Disposition", "attachment; filename="+URLEncoder.encode(fvo.getOriName(),"utf-8"));
			resp.setHeader("Content-Transfer-Encoding", "binary");
			resp.setHeader("Pragma", "no-cache");
			resp.setHeader("Cache-Control", "private");
			
			// response 객체 파일 스트림 작업
			String filePath = req.getServletContext().getRealPath("/file");
			File file = new File(filePath+"/"+fvo.getNewName());
			
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
			BufferedOutputStream bos = new BufferedOutputStream(resp.getOutputStream());
			
			while(true){
				int data = bis.read();
				// 더이상 읽을 데이터가 없을 경우
				if(data == -1){
					break;
				}
				bos.write(data);
			}
			bos.close();
			bis.close();
			
			
		}else {
			// �ش� View�� forward �ϱ�
			RequestDispatcher dispatcher = req.getRequestDispatcher(result);
			dispatcher.forward(req, resp);
		}
	}
	
}


















