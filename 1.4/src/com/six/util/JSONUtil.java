package com.six.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class JSONUtil<T>
{	
	//讲一个数组转化为一个json数据
	public static String jSONFromArray(String[] values)
	{
		return JSONArray.fromObject(values).toString();
	}
	//讲一个集合转化为json数据
	public static String JSONFromList(List<?> list)
	{
		return JSONArray.fromObject(list).toString();
	}
	
	/**
	 * 为操作成功返回Json
	 * @param strData
	 * @return
	 */
	public static String returnResultJson(String type,String strData){
		return new JSONObject().element("type", type).element("message", strData).toString();
	}
	
	/**
	 * 返回签到下拉框 json
	 * @return
	 */
	public String getAutoComboName(List<T> lists, String idName, String nameStr)
	{
		if(null == lists || lists.size() == 0)
			return "";
		
		@SuppressWarnings("unchecked")
		Class<T> clazz = (Class<T>)lists.get(0).getClass();
		
		//构建get方法
		String getMethodId = "get" + idName.substring(0, 1).toUpperCase() + idName.substring(1);
		String getMethodName = "get" + nameStr.substring(0, 1).toUpperCase() + nameStr.substring(1);
		JSONArray jsonArray = new JSONArray();
		try
		{
			Method idMethod = clazz.getMethod(getMethodId);
			Method nameMethod = clazz.getMethod(getMethodName);
			
			for(Object object : lists)
			{
				JSONObject jsonObject = new JSONObject();
				jsonObject.element("id", idMethod.invoke(object)).element("name", nameMethod.invoke(object));
				
				jsonArray.add(jsonObject);
			}
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
			e.printStackTrace();
			return "";
		} 
		
		return jsonArray.toString();
	}
	
	/**
	 * 构建数据表格 行数据
	 * @return 若返回为""则是获取失败
	 */
	public String getDatagridRow(int tatal,List<T> dataList,List<String> excludes)
	{
		if(dataList.size() == 0 || null == dataList)
			return "";
		Class<T> clazz = (Class<T>) dataList.get(0).getClass();
		JSONObject jsonObject = new JSONObject();
		jsonObject.element("total", tatal);
		JSONArray jsonArray = new JSONArray();
		
 		Field[] fields = clazz.getDeclaredFields();
		List<Field> fieldsList=new ArrayList<Field>(Arrays.asList(fields));
		
		//排除 字段的剔除
		if(null !=excludes && 0 != excludes.size())
		{
			List<Field> excludeFields = new ArrayList<Field>();//要删除的Field 集合
			
			for(Field field : fieldsList)
			{
				if(excludes.contains(field.getName()))
					excludeFields.add(field);
			}
			
			fieldsList.removeAll(excludeFields);
		}
		
		//构建 rows:[] json
		for(T obj : dataList)
		{
			JSONObject objJsonObject = new JSONObject();
			for(Field field : fieldsList)
			{
				String fieldName = field.getName();
				String getMethodStr = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
				
				try
				{
					Method getMethod = clazz.getMethod(getMethodStr);
					Object result = getMethod.invoke(obj);
					if(result != null)
						objJsonObject.element(fieldName, result.toString());
					
				} catch (Exception e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
				
			}
			
			jsonArray.add(objJsonObject);
		}
		
		jsonObject.element("rows", jsonArray);
		return jsonObject.toString();
	}
	
	
	public String getConstantJSON(Map<String, String> map)
	{
		
		Set<Entry<String, String>> entrySet = map.entrySet();
		
		JSONArray jsonArray = new JSONArray();
		for(Entry<String, String> entry : entrySet)
		{
			JSONObject jsonObject = new JSONObject();
			jsonObject.element("value", entry.getKey());
			jsonObject.element("text", entry.getValue());
			
			jsonArray.add(jsonObject);
		}
		
		return jsonArray.toString();
	}
	
	
}
