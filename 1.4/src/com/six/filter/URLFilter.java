package com.six.filter;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class URLFilter implements Filter {   
    public void doFilter(ServletRequest servletRequest, ServletResponse       
                              servletResponse, FilterChain filterChain) throws    
                                        IOException,ServletException   
    {   
       HttpServletRequest request = (HttpServletRequest) servletRequest;   
       HttpServletResponse response = (HttpServletResponse) servletResponse;   
       String realPath   
                    =request.getSession().getServletContext().getRealPath("/");   
  
       String fileName = realPath + "WEB-INF\\urlrewrite.xml";   
       String uri = request.getServletPath();   
       System.out.println("Furi:--"+uri);
       String rewriteUrl = getRewriteUrl(uri, fileName); 

       System.out.println("filename:--"+fileName);
       System.out.println( "rewriteUri:"+rewriteUrl); 
       if (null != rewriteUrl && !uri.endsWith("css")
				&& !uri.endsWith("js") && !uri.endsWith("png") 
				&& !uri.endsWith("businessmanLogin.jsp")
				&& !uri.startsWith("/business")
				&& !uri.startsWith("/core/login")) 
       {   
           request.getRequestDispatcher(rewriteUrl).forward(request, response);   
           return;   
       }   
       filterChain.doFilter(servletRequest, servletResponse);     
    }   
  
    private String getRewriteUrl(String url, String fileName) {   
       DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();   
       try {   
           DocumentBuilder builder = f.newDocumentBuilder();   
           Document document = builder.parse(fileName);   
           NodeList list = document.getElementsByTagName("rule");   
           for (int i = 0; i < list.getLength(); i++) {   
              Element elemnt = (Element) list.item(i);   
              NodeList list2 = elemnt.getElementsByTagName("from");   
              Element element = (Element) list2.item(0);   
              String formValue = element.getFirstChild().getNodeValue();   
              NodeList list3 = elemnt.getElementsByTagName("to");   
              Element element2 = (Element) list3.item(0);   
              String type = element2.getAttribute("type");   
              String toValue = element2.getFirstChild().getNodeValue();   
              String rewriteUrl = url.replaceAll(formValue, toValue);   
              if (url != null && !"".equals(url.trim()) &&   
                  !url.equals(rewriteUrl)) {   
                  return rewriteUrl;   
              }   
           }   
       } catch (Exception ex) {   
           ex.printStackTrace();   
       }   
       return null;   
    }

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
		
	}   
}   