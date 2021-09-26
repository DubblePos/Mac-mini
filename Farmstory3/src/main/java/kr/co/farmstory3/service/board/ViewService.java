package kr.co.farmstory3.service.board;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import kr.co.farmstory3.dao.ArticleDao;
import kr.co.farmstory3.service.CommonService;
import kr.co.farmstory3.vo.ArticleVo;
import kr.co.farmstory3.vo.MemberVo;

public class ViewService implements CommonService {

	@Override
	public String requestProc(HttpServletRequest req, HttpServletResponse resp) {
		
		HttpSession sess = req.getSession();
		MemberVo sessMember = (MemberVo) sess.getAttribute("sessMember");
		if(sessMember == null) {
			
			return "redirect:/member/login.do?success=102";	
		}
		
		String group = req.getParameter("group");
		String cate  = req.getParameter("cate");
		String seq   = req.getParameter("seq");
	
		
		ArticleDao dao = ArticleDao.getInstance();
		ArticleVo vo = dao.selectArticle(seq);
		List<ArticleVo> comments = dao.selectComments(seq);
		
		req.setAttribute("vo", vo);
		req.setAttribute("comments", comments);
		req.setAttribute("group", group);
		req.setAttribute("cate", cate);
		
		
		ArticleDao.getInstance().selectArticle(seq);
		
		
		
		return "/board/view.jsp";
	}

}