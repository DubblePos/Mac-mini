package kr.co.farmstory3.service.introduction;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.co.farmstory3.service.CommonService;

public class DirectionService implements CommonService{

	@Override
	public String requestProc(HttpServletRequest req, HttpServletResponse resp) {
		return "/introduction/direction.jsp";
	}

}
