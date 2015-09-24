package com.six.action;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;
import org.springframework.stereotype.Component;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

@Component
public abstract class BaseAction extends ActionSupport implements ModelDriven<Object>, ServletResponseAware,ServletRequestAware,SessionAware
{
	//servlet对象
	private Map<String,Object> sessionMap;
	private HttpServletRequest request;
	private HttpServletResponse response;
	
	//一些公共属性
	/**实体全名称*/
	protected String modelName;
	/** 表名*/
	protected String tableName;
	/**主键名*/
	protected String pkName;
	/**主键值*/
	protected String pkValue;
	/**第几页*/
	protected int start=1;
	/**每页几条*/
	protected int limit=10;
	/**排序*/
	protected String sort;
	/**查询条件*/
	protected String whereSql="";
	/** 排序条件*/
	protected String orderSql="";
	/**主键值列表*/
	protected String ids;
	/**传输字符串*/
	protected String strData;
	/**外键**/
	protected String foreignKey;
	/**为了json排除的字段*/
	protected String excludes="";  //checked
	protected Boolean expanded;
	protected int page = 1;
	protected int rows = 10;
	
	/**
	 * 回写内容到客户端
	 * @param content
	 * @throws IOException
	 */
	protected void toWrite(String content)
	{
		if(null != response)
		{
			response.setContentType("text/html;charset=utf-8");
			Writer writer = null;
			try
			{
				writer = response.getWriter();
				writer.write(content);
			} catch (IOException e)
			{
				// TODO Auto-generated catch block
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
			finally
			{
				if(null != writer)
				{
					try
					{
						writer.flush();
						writer.close();
						response.flushBuffer();
					} catch (IOException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
				
			}
			
		}
	}
	
	/**
	 * 跳转到指定链接
	 * @param url
	 * @param request
	 * @param response
	 * @throws Exception
	 * @throws IOException
	 */
	public void forword(String url,HttpServletRequest request, HttpServletResponse response) throws Exception, IOException
	{
		getRequest().getRequestDispatcher(url).forward(request, response);
	}
	
	public Map<String, Object> getSessionMap()
	{
		return sessionMap;
	}

	public void setSessionMap(Map<String, Object> sessionMap)
	{
		this.sessionMap = sessionMap;
	}

	public HttpServletRequest getRequest()
	{
		return request;
	}

	public void setRequest(HttpServletRequest request)
	{
		this.request = request;
	}

	public HttpServletResponse getResponse()
	{
		return response;
	}

	public void setResponse(HttpServletResponse response)
	{
		this.response = response;
	}

	public String getModelName()
	{
		return modelName;
	}

	public void setModelName(String modelName)
	{
		this.modelName = modelName;
	}

	public String getTableName()
	{
		return tableName;
	}

	public void setTableName(String tableName)
	{
		this.tableName = tableName;
	}

	public String getPkName()
	{
		return pkName;
	}

	public void setPkName(String pkName)
	{
		this.pkName = pkName;
	}

	public String getPkValue()
	{
		return pkValue;
	}

	public void setPkValue(String pkValue)
	{
		this.pkValue = pkValue;
	}

	public int getStart()
	{
		return start;
	}

	public void setStart(int start)
	{
		this.start = start;
	}

	public int getLimit()
	{
		return limit;
	}

	public void setLimit(int limit)
	{
		this.limit = limit;
	}

	public String getSort()
	{
		return sort;
	}

	public void setSort(String sort)
	{
		this.sort = sort;
	}

	public String getWhereSql()
	{
		return whereSql;
	}

	public void setWhereSql(String whereSql)
	{
		this.whereSql = whereSql;
	}

	public String getOrderSql()
	{
		return orderSql;
	}

	public void setOrderSql(String orderSql)
	{
		this.orderSql = orderSql;
	}

	public String getIds()
	{
		return ids;
	}

	public void setIds(String ids)
	{
		this.ids = ids;
	}

	public String getStrData()
	{
		return strData;
	}

	public void setStrData(String strData)
	{
		this.strData = strData;
	}

	public String getForeignKey()
	{
		return foreignKey;
	}

	public void setForeignKey(String foreignKey)
	{
		this.foreignKey = foreignKey;
	}

	public String getExcludes()
	{
		return excludes;
	}

	public void setExcludes(String excludes)
	{
		this.excludes = excludes;
	}

	public Boolean getExpanded()
	{
		return expanded;
	}

	public void setExpanded(Boolean expanded)
	{
		this.expanded = expanded;
	}

	@Override
	public void setSession(Map<String, Object> arg0)
	{
		this.sessionMap = arg0;
	}

	@Override
	public void setServletRequest(HttpServletRequest arg0)
	{
		this.request = arg0;
		
	}

	@Override
	public void setServletResponse(HttpServletResponse arg0)
	{
		this.response = arg0;
	}

	public int getPage()
	{
		return page;
	}

	public void setPage(int page)
	{
		this.page = page;
	}

	public int getRows()
	{
		return rows;
	}

	public void setRows(int rows)
	{
		this.rows = rows;
	}
	
}
