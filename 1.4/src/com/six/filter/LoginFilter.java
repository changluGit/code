package com.six.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;

import com.six.entity.User;
import com.six.util.StringUtil;

public class LoginFilter extends HttpServlet implements Filter{


	@Override
	public void doFilter(ServletRequest sRequest, ServletResponse sresponse,
			FilterChain filterChain) throws IOException, ServletException {
		// TODO Auto-generated method stub
		
		HttpServletRequest request = (HttpServletRequest) sRequest;
		HttpServletResponse response =(HttpServletResponse) sresponse;
		HttpSession session = request.getSession();
		String contextPath = request.getContextPath();
		String url=request.getServletPath();
		System.out.println("url:"+url);
		if(url.equals(""))
			url+="/";
			if((url.startsWith("/") && !url.startsWith("/login")) && !url.endsWith("css")
					&& !url.endsWith("js") && !url.endsWith("png")
					&& !url.endsWith("businessmanLogin.jsp")
					&& !url.startsWith("/business")
					&& !url.startsWith("/core/login"))
			{
				User user = (User) session.getAttribute("loginedUser");
				if(null==user)
				{
					response.sendRedirect(contextPath+"/login");
					return;
				}
				else{
					filterChain.doFilter(sRequest, sresponse);
				}
			}
			else{  
				filterChain.doFilter(sRequest, sresponse);
			}
			
	}

	@Override
	public void init(FilterConfig fc) throws ServletException {
		// TODO Auto-generated method stub
		
	}

}
