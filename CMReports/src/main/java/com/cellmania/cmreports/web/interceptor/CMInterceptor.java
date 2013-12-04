package com.cellmania.cmreports.web.interceptor;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts2.StrutsStatics;

import com.cellmania.cmreports.db.masters.UserMasterDTO;
import com.cellmania.cmreports.db.masters.UserParams;
import com.cellmania.cmreports.scheduler.ScheduleReport;
import com.cellmania.cmreports.web.util.CMDBService;
import com.cellmania.cmreports.web.util.SessionAttributeConstant;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

@SuppressWarnings("serial")
public class CMInterceptor extends AbstractInterceptor implements StrutsStatics {
	
	
	public static Logger log = Logger.getLogger(CMInterceptor.class);
	
	@Override
	public void destroy() {
		log.info("CM Report WebApplication : CLOSED");
		try {
			ScheduleReport.stopSchedular(false);
			new CMDBService().logoutAllUsers();
		} catch (Exception e) {
			log.error("Error updating all user Status whiel Server termination",e);
		}
	}
	
	public void init () {
		log.info ("Intializing CM Interceptor");
		
	}
	
	
	
	public String intercept(ActionInvocation invocation) throws Exception {
		
		final ActionContext context = invocation.getInvocationContext ();
		HttpServletRequest request = (HttpServletRequest) context.get(HTTP_REQUEST);
		log.info("Incoming request: "+request.getServletPath());
		request.setAttribute("contextPath", request.getContextPath());
		request.setAttribute("CONTEXT", request.getContextPath());
		request.setAttribute("sessionTimeOut", "1800");
		if(request.getSession().getAttribute(SessionAttributeConstant._LOGIN_USER)==null 
				&& !"/login.do".equals(request.getServletPath())
				&& !"/forgotPassword.do".equals(request.getServletPath())
				&& !"/recoverPassword.do".equals(request.getServletPath())
				&& !"/authenticate.do".equals(request.getServletPath())
				&& !"/resetPassword.do".equals(request.getServletPath())
				&& !"/doReset.do".equals(request.getServletPath())
				&& !"/paswordExpired.do".equals(request.getServletPath())
				&& !"/changePassword.do".equals(request.getServletPath())
				&& !"/dlFile.do".equals(request.getServletPath())
				){
			if(isAjaxRequest(request)){
				return "ajaxLoginPage";
			} else 
				return "loginPage";
		}
		if(request.getParameter("chngLoginUserId")!=null){
			UserMasterDTO originalUser = (UserMasterDTO) request.getSession().getAttribute(SessionAttributeConstant._ORIGINAL_LOGIN_USER);
			UserMasterDTO loginAsUser = (UserMasterDTO) request.getSession().getAttribute(SessionAttributeConstant._LOGIN_USER);
			long newUser = Long.parseLong(request.getParameter("chngLoginUserId"));
			if(originalUser==null){
				request.getSession().setAttribute(SessionAttributeConstant._ORIGINAL_LOGIN_USER,loginAsUser);
				originalUser = (UserMasterDTO) request.getSession().getAttribute(SessionAttributeConstant._ORIGINAL_LOGIN_USER);
			}
			if(newUser==0){
				request.getSession().setAttribute(SessionAttributeConstant._LOGIN_USER,originalUser);
			}
			else if(loginAsUser.getUserId().longValue() != newUser && originalUser.getRole().getName().equals("Administrator")){
				loginAsUser = new CMDBService().getUser(new UserParams(Long.parseLong(request.getParameter("chngLoginUserId"))));
				loginAsUser.setUserTimeZoneOffset(originalUser.getUserTimeZoneOffset());
				request.getSession().setAttribute(SessionAttributeConstant._LOGIN_USER,loginAsUser);
			}
			
		}
		return invocation.invoke ();
	}

	private boolean isAjaxRequest(HttpServletRequest request) {
		String xRequest = request.getHeader("X-Requested-With");
		String xReferal = request.getHeader("Referer");
		if(null == xRequest || xRequest.trim().length()==0 || null == xReferal || xReferal.trim().length()==0)
			return false;
		return true;
	}

}
