package kr.co.farmstory3.service.user;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import kr.co.farmstory3.dao.MemberDao;
import kr.co.farmstory3.service.CommonService;
import kr.co.farmstory3.vo.MemberVo;

public class LoginService implements CommonService {

	@Override
	public String requestProc(HttpServletRequest req, HttpServletResponse resp) {
		
		if(req.getMethod().equals("GET")) {
			
			String success = req.getParameter("success");
			req.setAttribute("success", success);
			
			return "/member/login.jsp";	
			
		}else {
			String uid  = req.getParameter("uid");
			String pass = req.getParameter("pass");
			
			MemberVo vo = MemberDao.getInstance().selectMember(uid, pass);
			
			if(vo != null) {
				HttpSession sess = req.getSession();
				sess.setAttribute("sessMember", vo);
				
				return "redirect:/";
			}else {
				return "redirect:/member/login.do?success=100";
			}
		}
	}
}
