package com.doudou.dispatch.trip.commons;

import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


public class AppWriteJson {
	
	/**
	 * 输出字符串
	 * @param response
	 * @param json
	 */
	public static void writeJson(HttpServletResponse response,String json){
		response.setContentType("application/json;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			out.write(json);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(null != out) out.close();
		}
		 
	}
	
	/**
	 * 输出app响应bean
	 */
	public static void writeJson(HttpServletResponse response,Object bean){
		response.setContentType("application/json;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			String json = JSONObject.toJSONString(bean);
			out.write(json);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if( null != out)out.close();
		}
	}
	
}
