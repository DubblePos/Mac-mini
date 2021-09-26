package kr.co.farmstory3.service.board;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

import kr.co.farmstory3.dao.ArticleDao;
import kr.co.farmstory3.service.CommonService;
import kr.co.farmstory3.vo.ArticleVo;

public class WriteService implements CommonService {
	private String path = null;
	@Override
	public String requestProc(HttpServletRequest req, HttpServletResponse resp) {
		String group = req.getParameter("group");
		String cate  = req.getParameter("cate");
		
		req.setAttribute("group", group);
		req.setAttribute("cate", cate);
		
		if(req.getMethod().equals("GET")) {
			
			return "/board/write.jsp?group="+group+"&cate="+cate;
			
		}else {			
			MultipartRequest mReq = getMultipartRequest(req);
			String title   = mReq.getParameter("title");
			String content = mReq.getParameter("content");
			String uid     = mReq.getParameter("uid");
			String fname   = mReq.getFilesystemName("fname");
			String mgroup  = mReq.getParameter("group");
			String mcate   = mReq.getParameter("cate");
			String regip   = req.getRemoteAddr();
			
			ArticleVo vo = new ArticleVo();
			vo.setCate(mcate);
			vo.setTitle(title);
			vo.setContent(content);
			vo.setFile(fname == null ? 0 : 1);
			vo.setUid(uid);
			vo.setRegip(regip);
			
			int seq = ArticleDao.getInstance().insertArticle(vo);

			if(fname != null) {
				// 파일첨부 했으면 이름수정하고 INSERT
				String newName = renameFile(fname, uid);
				ArticleDao.getInstance().insertFile(seq, fname, newName);
			}
			
			return "redirect:/board/list.do?group="+mgroup+"&cate="+mcate;
			
		}
	} // requestProc end

public MultipartRequest getMultipartRequest(HttpServletRequest req) {
		
		MultipartRequest mReq = null;
		
		try {
			path = req.getServletContext().getRealPath("/file");
			int maxSize = 1024 * 1024 * 10; // 최대 파일 허용크기 10MB
			
			mReq = new MultipartRequest(req, path, maxSize, "UTF-8", new DefaultFileRenamePolicy());
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return mReq;
	} // getMultipartRequest end
	
	public String renameFile(String fname, String uid) {
		// 파일명 수정
		int i = fname.lastIndexOf(".");
		String ext = fname.substring(i);
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss_");
		String now = sdf.format(new Date());
		String newName = now+uid+ext;
		
		File oriFile = new File(path+"/"+fname);
		File newFile = new File(path+"/"+newName);
		
		oriFile.renameTo(newFile);
		
		return newName;
	} // renameFile end
}