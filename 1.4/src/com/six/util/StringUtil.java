package com.six.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class StringUtil
{
	/**
	 * 构建查询字符串
	 * @param clazz
	 * @param where
	 * @param propertyName
	 * @param isAsc
	 * @return
	 */
	public static String getQueryStr(String where, String propertyName, boolean isAsc)
	{
		StringBuffer strBuffer = new StringBuffer();
		if(null != where)
		{
			strBuffer.append(" where" +" 1=1 and " + where);
		}
		
		if(null != propertyName)
		{
			if(isAsc)
				strBuffer.append(" order by " + propertyName +" asc");
			else
				strBuffer.append(" order by " + propertyName +" desc");
		}
		
		return strBuffer.toString();
	}
	/**
	 * 判断是否为整数
	 * @param number
	 * @return
	 */
	public static boolean isInteger(String number)
	{
		try
		{
			Integer.parseInt(number);
		} catch (NumberFormatException e)
		{
			return false;
		}
		
		return true;
		
	}
	
	/**
	 * 判断是否为空
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(String str)
	{
		if(null != str && str.trim().equals("")&&str.trim().equalsIgnoreCase("null"))
			return true;
		return false;
	}
	
	/**
	 * 用指定delim分割字符串
	 * @param str
	 * @return 一个装满分割后字符串的list
	 */
	public static List getValues(String str,String delim)
	{
		List list = new ArrayList();
		StringTokenizer stringTokenizer  = new StringTokenizer(str, ",");
		
		while(stringTokenizer.hasMoreElements())
			list.add(stringTokenizer.nextElement());
		
		return list;
	}
	
	/**
	 * 判断邮箱的格式是否正确
	 * @param email
	 * @return
	 */
	public static boolean emailFormat(String email)  
    {  
        boolean tag = true;  
        final String pattern1 = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";  
        final Pattern pattern = Pattern.compile(pattern1);  
        final Matcher mat = pattern.matcher(email);  
        if (!mat.find()) {  
            tag = false;  
        }  
        return tag;  
    }  
}  
	

