package com.doudou.dispatch.trip.configs;

import com.dispatch.gps.commons.bean.HttpApiConstans;
import com.dispatch.gps.commons.bean.JsonResult;
import com.dispatch.gps.commons.utils.OperationThreadLocal;
import com.doudou.dispatch.trip.api.entities.TUser;
import com.doudou.dispatch.trip.api.services.TUserService;
import com.doudou.dispatch.trip.commons.AppWriteJson;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@Slf4j
public class LoginInterceptor implements HandlerInterceptor{
	
	@Autowired
	private TUserService tUserService;
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		OperationThreadLocal.remove();
	}

	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
			throws Exception {
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String requestURI =  request.getRequestURI();
		//OPTIONS
		if(request.getMethod().equals("OPTIONS")) {
			return true;
		}
		
		String userName = request.getHeader("Authorization");
		log.info("userName = " + userName);
		TUser tUser = StringUtils.isNotEmpty(userName) == true ? tUserService.getSessionTUser(userName) : null;
		
		String accessSource = request.getHeader("AccessSource");
		if(StringUtils.isNotBlank(accessSource) && "wechat".equals(accessSource)) {
			return true;
		}
		
		if(requestURI.indexOf("/data") != -1 ) {
			
			if(null == tUser){
				JsonResult jsonResult = new JsonResult();
				jsonResult.setCode(HttpApiConstans.not_login);
				AppWriteJson.writeJson(response, jsonResult);
				return false;
			}
			
			tUserService.extensionLoginTime(tUser.getUserName());
		}
		return true;
	}

}
