package com.cellmania.cmreports.web.action;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.CookiesAware;
import org.apache.struts2.interceptor.ParameterAware;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;

import com.cellmania.cmreports.common.CMException;
import com.cellmania.cmreports.db.masters.UserMasterDTO;
import com.cellmania.cmreports.web.util.CMDBService;
import com.cellmania.cmreports.web.util.ServerSettingsConstants;
import com.cellmania.cmreports.web.util.SessionAttributeConstant;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

@SuppressWarnings( { "serial", "rawtypes" })
public class WebBaseAction  extends ActionSupport implements ParameterAware,
Action, Preparable, ServletRequestAware, ServletResponseAware,
SessionAware, CookiesAware {
	private HttpServletRequest request;
	private HttpServletResponse response;
	private Map session;
	private Map cookiesMap;
	private Map parameterMap;
	private String prevUrl=null;
	private String urlToRedirect=null;
	
	private int numberOfPagesToDisplay = 5;
	
	private static long adminRoleId;
	static {
		try {
			adminRoleId = Long.parseLong(CMDBService.getServerSettingsValue(ServerSettingsConstants._USER_ADMIN_ROLE_ID));
		} catch (CMException e) {
			e.printStackTrace();
		}
	}
	
	private int selectedMenuIndex = 1;
	
	private int pageStartIndex = 0;
	private int pageTotalRecCount = 0;
	private int perPageRecCount = 10;
	
	private UserMasterDTO loggedInUser;
	private String usrTimeZone;
	
	public void prepare() throws Exception {
		loggedInUser = (UserMasterDTO) getServletRequest().getSession().getAttribute(SessionAttributeConstant._LOGIN_USER);
		usrTimeZone = (String) getServletRequest().getSession().getAttribute(SessionAttributeConstant._LOGIN_USER_TIMEZONE_ID);
		return;
	}
	
	public String getPrevUrl() {
		return prevUrl;
	}

	public void setPrevUrl(String prevUrl) {
		this.prevUrl = prevUrl;
	}

	public String getUrlToRedirect() {
		return urlToRedirect;
	}

	public void setUrlToRedirect(String urlToRedirect) {
		this.urlToRedirect = urlToRedirect;
	}

	public int getNumberOfPagesToDisplay() {
		return numberOfPagesToDisplay;
	}

	public void setNumberOfPagesToDisplay(int numberOfPagesToDisplay) {
		this.numberOfPagesToDisplay = numberOfPagesToDisplay;
	}

	public static long getAdminRoleId() {
		return adminRoleId;
	}

	public int getSelectedMenuIndex() {
		return selectedMenuIndex;
	}

	public void setSelectedMenuIndex(int selectedMenuIndex) {
		this.selectedMenuIndex = selectedMenuIndex;
	}

	public UserMasterDTO getLoggedInUser() {
		if(this.loggedInUser == null){
			loggedInUser = (UserMasterDTO) getServletRequest().getSession().getAttribute(SessionAttributeConstant._LOGIN_USER);
		}
		return loggedInUser;
	}

	public void setLoggedInUser(UserMasterDTO loggedInUser) {
		this.loggedInUser = loggedInUser;
		getServletRequest().getSession().setAttribute(SessionAttributeConstant._LOGIN_USER,this.loggedInUser);
	}

	public String getUsrTimeZone() {
		return usrTimeZone;
	}

	public void setUsrTimeZone(String usrTimeZone) {
		this.usrTimeZone = usrTimeZone;
	}

	public void setParameters(Map parameterMap) {
		this.parameterMap = parameterMap;
	}

	public Map getParameterMap() {
		return this.parameterMap;
	}
	public Map getCookiesMap() {
		return cookiesMap;
	}

	public void setCookiesMap(Map cookiesMap) {
		this.cookiesMap = cookiesMap;
	}

	public void setSession(Map session) {
		session = this.getSession();
	}

	public Map getSession() {
		return session;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public HttpServletRequest getServletRequest() {
		return request;
	}

	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}
	
	public HttpServletResponse getServletResponse() {
		return response;
	}

	public int getPageStartIndex() {
		return pageStartIndex;
	}

	public void setPageStartIndex(int pageStartIndex) {
		this.pageStartIndex = pageStartIndex;
	}

	public int getPageTotalRecCount() {
		return pageTotalRecCount;
	}

	public void setPageTotalRecCount(int pageTotalRecCount) {
		this.pageTotalRecCount = pageTotalRecCount;
	}

	public int getPerPageRecCount() {
		return perPageRecCount;
	}

	public void setPerPageRecCount(int perPageRecCount) {
		this.perPageRecCount = perPageRecCount;
	}
}
