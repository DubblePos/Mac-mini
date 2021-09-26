package kr.co.farmstory3.service.user;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import kr.co.farmstory3.dao.MemberDao;
import kr.co.farmstory3.service.CommonService;
import kr.co.farmstory3.vo.MemberVo;

public class LogoutService implements CommonService {

	@Override
	public String requestProc(HttpServletRequest req, HttpServletResponse resp) {
		
		HttpSession sess = req.getSession();
		sess.invalidate();
		
		return "redirect:/member/login.do?success=101";
	}
}
