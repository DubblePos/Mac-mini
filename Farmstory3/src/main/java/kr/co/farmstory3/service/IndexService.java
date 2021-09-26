package kr.co.farmstory3.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.co.farmstory3.dao.ArticleDao;
import kr.co.farmstory3.vo.ArticleVo;

public class IndexService implements CommonService{

	@Override
	public String requestProc(HttpServletRequest req, HttpServletResponse resp) {
		
		List<ArticleVo> articlesGrow = ArticleDao.getInstance().selectCroptalk("grow");
		
		req.setAttribute("articlesGrow", articlesGrow);
		
		List<ArticleVo> articlesSchool = ArticleDao.getInstance().selectCroptalk("school");
				
		req.setAttribute("articlesSchool", articlesSchool);
				
		List<ArticleVo> articlesStory = ArticleDao.getInstance().selectCroptalk("story");
		
		req.setAttribute("articlesStory", articlesStory);
		return "/index.jsp";
	}

}
