package com.six.util;

import java.io.IOException;
import java.util.Properties;

import org.springframework.core.io.support.PropertiesLoaderUtils;

/**
 * 读取系统配置工具类
 * 
 * 
 * @author Admin
 *
 */
public class PropertiesUtil
{
	public static Properties properties;
	static{
		
		try
		{
			properties = PropertiesLoaderUtils.loadAllProperties("sysproperties.properties");
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static String getValue(String key)
	{
		
		return properties.getProperty(key);
	}
}
