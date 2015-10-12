package com.sf.module.gswmgmt.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.sf.framework.base.IPage;
import com.sf.module.frameworkimpl.dao.BaseJdbcDao;
import com.sf.module.frameworkimpl.domain.QueryObj;
import com.sf.module.gswmgmt.domain.ProductType;
import com.sf.module.gswmgmt.domain.ReceivedDetail;
import com.sf.module.gswmgmt.util.CommonUtil;

/**
 * <pre>
 * *********************************************
 * Copyright sf-express.
 * All rights reserved.
 * Description: 收件汇总Dao
 * HISTORY
 * *********************************************
 *  ID     DATE          PERSON          REASON
 *  1      2014-03-06    sfhq217         创建
 * *********************************************
 * </pre>
 */
public class ReceivedDetailDao extends BaseJdbcDao {

	/**
	 * 加载所有的产品类型
	 * 
	 * @return List<ProductType>
	 */
	public List<ProductType> findAllProductType() {
		String sql = "select id, type_code||'('||decode(type_name,'','未知',type_name)||')' type_name, type_code from tm_product_type t order by id";
		return this.queryForList(sql, new Object[] {}, ProductType.class);
	}

	/**
	 * 分页查询
	 * 
	 * @param queryObj
	 * @return IPage<ReceivedDetail>
	 */
	public IPage<ReceivedDetail> findPageBy(QueryObj queryObj, int pageSize,
			int pageIndex) {
		Map<String, Object> map = checkStatisticsWay(queryObj);
		return this.fetchPage(((StringBuilder) map.get("sql")),
				(Object[]) map.get("params"), queryObj.getPageSize(),
				queryObj.getPageSize() * queryObj.getPageIndex(),
				ReceivedDetail.class);
	}

	/**
	 * 收件汇总导出
	 * 
	 * @param deptCode
	 * @return List<PredictedReceivedDTO>
	 */
	public List<ReceivedDetail> listExport(QueryObj queryObj) {
		Map<String, Object> map = checkStatisticsWay(queryObj);
		return this.queryForList((map.get("sql")).toString(),
				(Object[]) map.get("params"), ReceivedDetail.class);
	}

	private Map<String, Object> checkStatisticsWay(QueryObj queryObj) {
		String way = queryObj.getQueryValue("statisticsWay"); // 统计方式
		Map<String, Object> map = null;

		switch (Integer.valueOf(way)) {
		case 1:
			map = this.getCondition1(queryObj); // 按寄件网点统计
			break;
		case 2:
			map = this.getCondition2(queryObj); // 按寄件时段统计
			break;
		case 3:
			map = this.getCondition3(queryObj); // 按到件目的地
			break;
		case 4:
			map = this.getCondition4(queryObj); // 按寄出流向
			break;
		}
		return map;
	}

	private Map<String, Object> getCondition1(QueryObj queryObj) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<Object> params = new ArrayList<Object>();
		StringBuilder sql = new StringBuilder();

		// 上门收件日期
		String receDt = queryObj.getQueryValue("receivedDeliveredTm");
		// 结束日期
		String endTm = queryObj.getQueryValue("endTm");
		// 产品类型
		String proType = queryObj.getQueryValue("productType");
		// 寄件网点代码
		String receDeptCode = queryObj.getQueryValue("receivedDeptCode");
		// 网点层级
		String typeLevel = queryObj.getQueryValue("receivedDeptLevel");
		// 寄件网点展示方式
		String receiveDeptShowType = queryObj
				.getQueryValue("receiveDeptShowType");
		// 汇总方式
		String theTotal = queryObj.getQueryValue("theTotal");
		// 寄件二级调度
		String sendZdCode = queryObj.getQueryValue("sendZdCode");
		// 寄件城市
		String cityCode = queryObj.getQueryValue("cityCode");

		// 目的地展示方式
		String destDeptShowType = queryObj.getQueryValue("destDeptShowType");

		// 筛选数据
		sql.append(" WITH TEMP AS (");

		sql.append(" SELECT T.*,SDM.BG_CODE SEND_BG_CODE,SDM.ZD_CODE SEND_ZD_CODE,SDM.DEPT_NAME DEPT_CODE_NAME,DT.CLASS_NAME DEPT_NAME,DT.TYPE_NAME CHILD_DEPT_NAME ");
		sql.append(" FROM TT_RECEIVED_STAT_DEPT T");
		sql.append(" LEFT JOIN TM_DEPARTMENT SDM ");
		sql.append(" ON (T.DEPT_CODE = SDM.DEPT_CODE)");
		sql.append(" LEFT JOIN TM_CDP_DEPT_TYPE DT ");
		sql.append(" ON (T.DEPT_TYPE=DT.TYPE_CODE)");

		
		sql.append(")");
		
		// 按照展示层级统计
		String queryColSQL = createSQLByLevel(receiveDeptShowType, destDeptShowType);
		sql.append("SELECT ");
		if(StringUtils.isNotEmpty(queryColSQL)){
			sql.append(queryColSQL).append(",");
		}
		if ("1".equals(theTotal)) {
			sql.append(" STAT_DT,"); // 汇总方式为单日（1），增加寄件日期
		}else if("2".equals(theTotal)){
			sql.append(" to_date(?,'YYYY-MM-DD') stat_Date,");
			params.add(receDt);
			sql.append(" to_date(?,'YYYY-MM-DD') end_Date,");
			params.add(endTm);
		}
		if(StringUtils.isNotBlank(proType)){
			sql.append(" PRODUCT_TYPE,");
		}
		sql.append(" SUM(DEL_NUM) DEL_NUM,");
		sql.append(" SUM(TDAY_PS) TDAY_PS ,");
		sql.append(" SUM(TMR1030_PS) TMR1030_PS ,");
		sql.append(" SUM(TMR_PS) TMR_PS ,");
		sql.append(" SUM(ATMR_PS) ATMR_PS,");
		sql.append(" SUM(OTHER_PS) OTHER_PS,");
		sql.append(" SUM(PIC_NUM) PIC_NUM,"); // 总派送量=当日派件量+次日派送量+隔日派送量+隔日以上派送量
		sql.append(" DECODE(SUM(PIC_NUM), 0, 0, ROUND(SUM(TDAY_PS) /SUM(PIC_NUM), 4)) TDAY_PS_RATE,");
		sql.append(" DECODE(SUM(PIC_NUM), 0, 0, ROUND(SUM(TMR1030_PS) /SUM(PIC_NUM), 4)) TMR1030_PS_RATE,");
		sql.append(" DECODE(SUM(PIC_NUM), 0, 0, ROUND(SUM(TMR_PS) /SUM(PIC_NUM), 4)) TMR_PS_RATE,");
		sql.append(" DECODE(SUM(PIC_NUM), 0, 0, ROUND(SUM(ATMR_PS) /SUM(PIC_NUM), 4)) ATMR_PS_RATE,");
		sql.append(" DECODE(SUM(PIC_NUM), 0, 0, ROUND( SUM(OTHER_PS) /SUM(PIC_NUM), 4)) OTHER_PS_RATE,");
		sql.append(" DECODE(SUM(PIC_NUM),0,0,ROUND(SUM(DEL_NUM) / SUM(PIC_NUM) , 4)) DEL_NUM_RATE,");
		sql.append(" SUM(NO_DEL_NUM) NO_DEL_NUM ");
		
		sql.append(" FROM TEMP");
		if (StringUtils.isNotBlank(receDt)) { // 收件开始日期
			sql.append(" WHERE STAT_DT >= TO_DATE(?, 'YYYY-MM-DD')");
			params.add(receDt);
		}

		if (StringUtils.isNotBlank(endTm)) { // 收件结束日期
			sql.append(" and STAT_DT <= TO_DATE(?, 'YYYY-MM-DD')");
			params.add(endTm);
		}
		if (StringUtils.isNotBlank(receDeptCode)
				&& !receDeptCode.equalsIgnoreCase("001")) { // 寄件网点代码, 且不为001
			sql.append(selectDeptCode(typeLevel));
			if (StringUtils.isNotBlank(receDeptCode)) {
				params.add(receDeptCode);
			}
		}
		if (StringUtils.isNotBlank(sendZdCode)) { // 寄件二级调度
			sql.append(" and SEND_ZD_CODE=?");
			params.add(sendZdCode);
		}
		if (StringUtils.isNotBlank(cityCode)) { // 寄件城市
			sql.append(" AND  CITY_CODE  in(");
			this.appendInCondition(sql, cityCode, params);
			sql.append(" )");
		}
		if (StringUtils.isNotBlank(proType)) { // 产品类型
			sql.append(" AND PRODUCT_TYPE IN(");
			this.appendInCondition(sql, proType, params);
			sql.append(" )");
		}
		
		if(StringUtils.isNotEmpty(queryColSQL)){
			// 分组
			if ("1".equals(theTotal)) {
				sql.append(" GROUP BY STAT_DT," + queryColSQL);
			}else if("2".equals(theTotal)){
				sql.append(" GROUP BY " + queryColSQL);
			}
			if(StringUtils.isNotBlank(proType)){
				sql.append(" ,PRODUCT_TYPE");
			}
			// 排序
			if ("1".equals(theTotal)) {
				sql.append(" ORDER BY STAT_DT desc," + queryColSQL);
			}else if("2".equals(theTotal)){
				sql.append(" ORDER BY " + queryColSQL);
			}
		}else {
			// 分组
			if ("1".equals(theTotal)) {
				sql.append(" GROUP BY STAT_DT" );
			}
			if(StringUtils.isNotBlank(proType)){
				sql.append(" ,PRODUCT_TYPE");
			}
			
			// 排序
			if ("1".equals(theTotal)) {
				sql.append(" ORDER BY STAT_DT desc" );
			}
		}
		
		
		map.put("sql", sql);
		map.put("params", params.toArray());

		return map;
	}

	private Map<String, Object> getCondition2(QueryObj queryObj) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<Object> params = new ArrayList<Object>();
		StringBuilder sql = new StringBuilder();

		// 上门收件日期
		String receDt = queryObj.getQueryValue("receivedDeliveredTm");
		// 结束日期
		String endTm = queryObj.getQueryValue("endTm");
		// 产品类型
		String proType = queryObj.getQueryValue("productType");
		// 寄件网点代码
		String receDeptCode = queryObj.getQueryValue("receivedDeptCode");
		// 网点层级
		String typeLevel = queryObj.getQueryValue("receivedDeptLevel");
		// 寄件网点展示方式
		String receiveDeptShowType = queryObj.getQueryValue("receiveDeptShowType");
		// 汇总方式
		String theTotal = queryObj.getQueryValue("theTotal");
		
		// 寄件二级调度
		String sendZdCode = queryObj.getQueryValue("sendZdCode");
		// 寄件城市
		String cityCode = queryObj.getQueryValue("cityCode");

		// 目的地展示方式
		String destDeptShowType = queryObj.getQueryValue("destDeptShowType");
		
		// 筛选数据
		sql.append(" WITH TEMP AS (");

		sql.append(" SELECT T.*,SDM.BG_CODE SEND_BG_CODE,SDM.ZD_CODE SEND_ZD_CODE ");
		sql.append(" FROM TT_RECEIVED_STAT_DEPT T");
		sql.append(" LEFT JOIN TM_DEPARTMENT SDM ");
		sql.append(" ON (T.DEPT_CODE = SDM.DEPT_CODE)");

		
		sql.append(")");
		
		// 按照展示层级统计
		String queryColSQL = createSQLByLevel(receiveDeptShowType, destDeptShowType);
		sql.append("SELECT ");
		if(StringUtils.isNotEmpty(queryColSQL)){
			sql.append(queryColSQL).append(",");
		}
		if ("1".equals(theTotal)) {
			sql.append(" STAT_DT,"); // 汇总方式为单日（1），增加寄件日期
		}else if("2".equals(theTotal)){
			sql.append(" to_date(?,'YYYY-MM-DD') stat_Date,");
			params.add(receDt);
			sql.append(" to_date(?,'YYYY-MM-DD') end_Date,");
			params.add(endTm);
		}
		if(StringUtils.isNotBlank(proType)){
			sql.append(" PRODUCT_TYPE,");
		}
		sql.append(" SUM(DEL_NUM) DEL_NUM,");
		sql.append(" SUM(TDAY_PS) TDAY_PS ,");
		sql.append(" SUM(TMR1030_PS) TMR1030_PS ,");
		sql.append(" SUM(TMR_PS) TMR_PS ,");
		sql.append(" SUM(ATMR_PS) ATMR_PS,");
		sql.append(" SUM(OTHER_PS) OTHER_PS,");
		sql.append(" SUM(PIC_NUM) PIC_NUM,"); // 总派送量=当日派件量+次日派送量+隔日派送量+隔日以上派送量
		sql.append(" DECODE(SUM(PIC_NUM), 0, 0, ROUND(SUM(TDAY_PS) /SUM(PIC_NUM), 4)) TDAY_PS_RATE,");
		sql.append(" DECODE(SUM(PIC_NUM), 0, 0, ROUND(SUM(TMR1030_PS) /SUM(PIC_NUM), 4)) TMR1030_PS_RATE,");
		sql.append(" DECODE(SUM(PIC_NUM), 0, 0, ROUND(SUM(TMR_PS) /SUM(PIC_NUM), 4)) TMR_PS_RATE,");
		sql.append(" DECODE(SUM(PIC_NUM), 0, 0, ROUND(SUM(ATMR_PS) /SUM(PIC_NUM), 4)) ATMR_PS_RATE,");
		sql.append(" DECODE(SUM(PIC_NUM), 0, 0, ROUND( SUM(OTHER_PS) /SUM(PIC_NUM), 4)) OTHER_PS_RATE,");
		sql.append(" DECODE(SUM(PIC_NUM),0,0,ROUND(SUM(DEL_NUM) / SUM(PIC_NUM) , 4)) DEL_NUM_RATE,");
		sql.append(" SUM(NO_DEL_NUM) NO_DEL_NUM ");
		
		sql.append(" FROM TEMP");
		
		if (StringUtils.isNotBlank(receDt)) { // 收件开始日期
			sql.append(" WHERE stat_dt >= to_date(?, 'YYYY-MM-DD')");
			params.add(receDt);
		}

		if (StringUtils.isNotBlank(endTm)) { // 收件结束日期
			sql.append(" and stat_dt <= to_date(?, 'YYYY-MM-DD')");
			params.add(endTm);
		}
		if (StringUtils.isNotBlank(receDeptCode)
				&& !receDeptCode.equalsIgnoreCase("001")) { // 寄件网点代码, 且不为001
			sql.append(selectDeptCode(typeLevel));
			if (StringUtils.isNotBlank(receDeptCode)) {
				params.add(receDeptCode);
			}
		}
		if (StringUtils.isNotBlank(sendZdCode)) { // 寄件二级调度
			sql.append(" and SEND_ZD_CODE=?");
			params.add(sendZdCode);
		}
		if (StringUtils.isNotBlank(cityCode)) { // 寄件城市
			sql.append(" AND  CITY_CODE  in(");
			this.appendInCondition(sql, cityCode, params);
			sql.append(" )");
		}
		if (StringUtils.isNotBlank(proType)) { // 产品类型
			sql.append(" and product_type in(");
			this.appendInCondition(sql, proType, params);
			sql.append(" )");
		}
		// 权限验证
        String userId = queryObj.getQueryValue("userId");
        sql.append(" AND DEPT_CODE IN (").append(CommonUtil.getUserAllDeptSQL(userId, params)).append(")");
        
		
		if(StringUtils.isNotEmpty(queryColSQL)){
			// 分组
			if ("1".equals(theTotal)) {
				sql.append(" GROUP BY STAT_DT," + queryColSQL);
			}else if("2".equals(theTotal)){
				sql.append(" GROUP BY " + queryColSQL);
			}
			if(StringUtils.isNotBlank(proType)){
				sql.append(" ,PRODUCT_TYPE");
			}
			
			// 排序
			if ("1".equals(theTotal)) {
				sql.append(" ORDER BY STAT_DT desc," + queryColSQL);
			}else if("2".equals(theTotal)){
				sql.append(" ORDER BY " + queryColSQL);
			}
		}else {
			// 分组
			if ("1".equals(theTotal)) {
				sql.append(" GROUP BY STAT_DT" );
			}
			if(StringUtils.isNotBlank(proType)){
				sql.append(" ,PRODUCT_TYPE");
			}
			// 排序
			if ("1".equals(theTotal)) {
				sql.append(" ORDER BY STAT_DT desc" );
			}
		}
		
		
		map.put("sql", sql);
		map.put("params", params.toArray());

		return map;
	}

	private Map<String, Object> getCondition3(QueryObj queryObj) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<Object> params = new ArrayList<Object>();
		StringBuilder sql = new StringBuilder();

		// 上门收件日期
		String receDt = queryObj.getQueryValue("receivedDeliveredTm");
		// 结束日期
		String endTm = queryObj.getQueryValue("endTm");
		// 目的地
		String destDeptCode = queryObj.getQueryValue("destDeptCode");
		// 目的城市
		String destCityCode = queryObj.getQueryValue("destCityCode");
		// 目的地展示方式
		String destDeptShowType = queryObj.getQueryValue("destDeptShowType");
		// 目的地网点层级
		String typeLevel = queryObj.getQueryValue("destDeptLevel");
		// 寄件网点展示方式
		String receiveDeptShowType = queryObj.getQueryValue("receiveDeptShowType");
		//目的二级调度
		String destZdCode = queryObj.getQueryValue("destZdCode");
		// 汇总方式
		String theTotal = queryObj.getQueryValue("theTotal");
		// 筛选数据
		sql.append(" WITH TEMP AS (");

		sql.append(" SELECT T.*,SDM.BG_CODE DEST_BG_CODE,SDM.ZD_CODE DEST_ZD_CODE ");
		sql.append(" FROM TT_RECEIVED_STAT_DEST T");
		sql.append(" LEFT JOIN TM_DEPARTMENT SDM ");
		sql.append(" ON (T.DEST_AREA_CODE = SDM.DEPT_CODE AND SDM.TYPE_LEVEL=2)");
		sql.append(")");
		
		// 按照展示层级统计
		String queryColSQL = createSQLByLevel(receiveDeptShowType, destDeptShowType);
		sql.append("SELECT ");
		if(StringUtils.isNotEmpty(queryColSQL)){
			sql.append(queryColSQL).append(",");
		}
		if ("1".equals(theTotal)) {
			sql.append(" STAT_DT,"); // 汇总方式为单日（1），增加寄件日期
		}else if("2".equals(theTotal)){
			sql.append(" to_date(?,'YYYY-MM-DD') stat_Date,");
			params.add(receDt);
			sql.append(" to_date(?,'YYYY-MM-DD') end_Date,");
			params.add(endTm);
		}
		sql.append(" sum(del_num) del_Num, " );
		sql.append(" SUM(TDAY_PS) TDAY_PS ,");
		sql.append(" SUM(TMR1030_PS) TMR1030_PS ,");
		sql.append(" SUM(TMR_PS) TMR_PS ,");
		sql.append(" SUM(ATMR_PS) ATMR_PS,");
		sql.append(" SUM(OTHER_PS) OTHER_PS,");
		sql.append(" SUM(PIC_NUM) PIC_NUM,");  //总派送量=当日派件量+次日派送量+隔日派送量+隔日以上派送量	
		sql.append(" DECODE(SUM(PIC_NUM), 0, 0, ROUND(SUM(TMR_PS) /SUM(PIC_NUM),4)) TMR_PS_RATE,");
		sql.append(" DECODE(SUM(PIC_NUM), 0, 0, ROUND(SUM(TMR1030_PS) /SUM(PIC_NUM),4)) TMR1030_PS_RATE,");
		sql.append(" DECODE(SUM(PIC_NUM), 0, 0, ROUND(SUM(ATMR_PS) /SUM(PIC_NUM),4)) ATMR_PS_RATE,");
		sql.append(" DECODE(SUM(PIC_NUM), 0, 0, ROUND( SUM(OTHER_PS) /SUM(PIC_NUM),4)) OTHER_PS_RATE,");
		sql.append(" DECODE(SUM(PIC_NUM),0,0,ROUND(SUM(DEL_NUM) / SUM(PIC_NUM), 4)) DEL_NUM_RATE,");
		sql.append(" SUM(NO_DEL_NUM) NO_DEL_NUM ");
		
		sql.append(" FROM TEMP");
		
		if (StringUtils.isNotBlank(receDt)) { // 收件开始日期
			sql.append(" where stat_dt >= to_date(?, 'YYYY-MM-DD')");
			params.add(receDt);
		}

		if (StringUtils.isNotBlank(endTm)) { // 收件结束日期
			sql.append(" and stat_dt <= to_date(?, 'YYYY-MM-DD')");
			params.add(endTm);
		}
		if (StringUtils.isNotBlank(destDeptCode)&& !destDeptCode.equalsIgnoreCase("001")) { // 目的网点代码, 且不为001
			sql.append(selectDestDeptCode(typeLevel));
			if (StringUtils.isNotBlank(selectDestDeptCode(typeLevel))) {
				params.add(destDeptCode);
			}
		}
		if (StringUtils.isNotBlank(destZdCode)) { // 目的二级调度
			sql.append(" AND DEST_ZD_CODE=?");
			params.add(destZdCode);
		}
		if (StringUtils.isNotBlank(destCityCode)) { // 目的城市
			sql.append(" AND DEST_CITY_CODE IN(");
			this.appendInCondition(sql, destCityCode, params);
			sql.append(" )");
		}
		
		 // 权限验证
        String userId = queryObj.getQueryValue("userId");
        sql.append(" AND DEST_AREA_CODE IN (").append(CommonUtil.getUserAllDeptSQL(userId, params)).append(")");
        

		if(StringUtils.isNotEmpty(queryColSQL)){
			// 分组
			if ("1".equals(theTotal)) {
				sql.append(" GROUP BY STAT_DT," + queryColSQL);
			}else if("2".equals(theTotal)){
				sql.append(" GROUP BY " + queryColSQL);
			}
			
			// 排序
			if ("1".equals(theTotal)) {
				sql.append(" ORDER BY STAT_DT desc," + queryColSQL);
			}else if("2".equals(theTotal)){
				sql.append(" ORDER BY " + queryColSQL);
			}
		}else {
			// 分组
			if ("1".equals(theTotal)) {
				sql.append(" GROUP BY STAT_DT" );
			}
			
			// 排序
			if ("1".equals(theTotal)) {
				sql.append(" ORDER BY STAT_DT desc" );
			}
		}
		
		
		map.put("sql", sql);
		map.put("params", params.toArray());

		return map;
		
	}

	private Map<String, Object> getCondition4(QueryObj queryObj) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<Object> params = new ArrayList<Object>();
		StringBuilder sql = new StringBuilder();

		// 上门收件日期
		String receDt = queryObj.getQueryValue("receivedDeliveredTm");
		// 结束日期
		String endTm = queryObj.getQueryValue("endTm");
		// 产品类型
		String proType = queryObj.getQueryValue("productType");
		// 寄件网点
		String receDeptCode = queryObj.getQueryValue("receivedDeptCode");
		// 寄件网点层级
		String rLevel = queryObj.getQueryValue("receivedDeptLevel");
		// 目的地
		String destDeptCode = queryObj.getQueryValue("destDeptCode");
		// 目的城市
		String destCityCode = queryObj.getQueryValue("destCityCode");
		// 目的地网点层级
		String dLevel = queryObj.getQueryValue("destDeptLevel");
		// 寄件网点展示方式
		String receiveDeptShowType = queryObj.getQueryValue("receiveDeptShowType");
		//目的二级调度
		String destZdCode = queryObj.getQueryValue("destZdCode");
		// 寄件二级调度
		String sendZdCode = queryObj.getQueryValue("sendZdCode");
		// 寄件城市
		String cityCode = queryObj.getQueryValue("cityCode");
		// 目的地展示方式
		String destDeptShowType = queryObj.getQueryValue("destDeptShowType");
		// 汇总方式
		String theTotal = queryObj.getQueryValue("theTotal");

//		String userDept = queryObj.getQueryValue("userDept");
//		String userLevel = queryObj.getQueryValue("userLevel");
		
		// 筛选数据
		sql.append(" WITH TEMP AS (");

		sql.append(" SELECT T.*,SDM.BG_CODE SEND_BG_CODE,SDM.ZD_CODE SEND_ZD_CODE,DDM.BG_CODE DEST_BG_CODE,DDM.ZD_CODE DEST_ZD_CODE ");
		sql.append(" FROM TT_RECEIVED_STAT_FRTO T");
		sql.append(" LEFT JOIN TM_DEPARTMENT SDM ");
		sql.append(" ON (T.DEST_AREA_CODE = SDM.DEPT_CODE AND SDM.TYPE_LEVEL=2)");
		sql.append(" LEFT JOIN TM_DEPARTMENT DDM ");
		sql.append(" ON (T.AREA_CODE = DDM.DEPT_CODE AND SDM.TYPE_LEVEL=2)");
		
		
//		sql.append(" AND (");
//		sql.append(" T.DEST_AREA_CODE in ({0})" + CommonUtil.);
//		
//		sql.append(" or");
//		sql.append(" T.AREA_CODE =");
//		sql.append(")");
//		
		
		sql.append(")");
		
		// 按照展示层级统计
		String queryColSQL = createSQLByLevel(receiveDeptShowType, destDeptShowType);
		sql.append("SELECT ");
		if(StringUtils.isNotEmpty(queryColSQL)){
			sql.append(queryColSQL).append(",");
		}
		if(StringUtils.isNotBlank(proType)){
			sql.append(" PRODUCT_TYPE,");
		}
		if ("1".equals(theTotal)) {
			sql.append(" STAT_DT,"); // 汇总方式为单日（1），增加寄件日期
		}else if("2".equals(theTotal)){
			sql.append(" to_date(?,'YYYY-MM-DD') stat_Date,");
			params.add(receDt);
			sql.append(" to_date(?,'YYYY-MM-DD') end_Date,");
			params.add(endTm);
		}
//		if(("5".equals(receiveDeptShowType)&&"5".equals(showType))||("5".equals(receiveDeptShowType)&&"6".equals(showType))||
//				("6".equals(receiveDeptShowType)&&"5".equals(showType))||("6".equals(receiveDeptShowType)&&"6".equals(showType))){
//			//如果始发地和目的地展示方式都为城市，增加流向类型
//			sql.append(" FLOW_TYPE, ");	
//		}
		sql.append(" sum(del_num) del_Num, ");
		sql.append(" SUM(TDAY_PS) TDAY_PS ,");
		sql.append(" SUM(TMR1030_PS) TMR1030_PS ,");
		sql.append(" SUM(TMR_PS) TMR_PS ,");
		sql.append(" SUM(ATMR_PS) ATMR_PS,");
		sql.append(" SUM(OTHER_PS) OTHER_PS,");
		sql.append(" SUM(PIC_NUM) PIC_NUM,"); // 总派送量=当日派件量+次日派送量+隔日派送量+隔日以上派送量
		sql.append(" DECODE(SUM(PIC_NUM), 0, 0, ROUND(SUM(TDAY_PS) /SUM(PIC_NUM),4)) TDAY_PS_RATE,");
		sql.append(" DECODE(SUM(PIC_NUM), 0, 0, ROUND(SUM(TMR1030_PS) /SUM(PIC_NUM),4)) TMR1030_PS_RATE,");
		sql.append(" DECODE(SUM(PIC_NUM), 0, 0, ROUND(SUM(TMR_PS) /SUM(PIC_NUM),4)) TMR_PS_RATE,");
		sql.append(" DECODE(SUM(PIC_NUM), 0, 0, ROUND(SUM(ATMR_PS) /SUM(PIC_NUM),4)) ATMR_PS_RATE,");
		sql.append(" DECODE(SUM(PIC_NUM), 0, 0, ROUND( SUM(OTHER_PS) /SUM(PIC_NUM),4)) OTHER_PS_RATE,");
		sql.append("  DECODE(SUM(PIC_NUM),0,0,ROUND(SUM(DEL_NUM) / SUM(PIC_NUM),4)) DEL_NUM_RATE,");
		sql.append(" SUM(NO_DEL_NUM) NO_DEL_NUM ");
		
		sql.append(" FROM TEMP");
		
		sql.append(" WHERE");

		if (StringUtils.isNotBlank(receDt)) { // 收件开始日期
			sql.append(" STAT_DT >= TO_DATE(?, 'YYYY-MM-DD')");
			params.add(receDt);
		}

		if (StringUtils.isNotBlank(endTm)) { // 收件结束日期
			sql.append(" AND STAT_DT <= TO_DATE(?, 'YYYY-MM-DD')");
			params.add(endTm);
		}
		if (StringUtils.isNotBlank(receDeptCode)&& !receDeptCode.equalsIgnoreCase("001")) { // 寄件网点代码, 且不为001
			sql.append(selectDeptCode(rLevel));
			if (StringUtils.isNotBlank(receDeptCode)) {
				params.add(receDeptCode);
			}
		}
		if (StringUtils.isNotBlank(destDeptCode)&& !destDeptCode.equalsIgnoreCase("001")) { // 目的网点代码, 且不为001
			sql.append(selectDestDeptCode(dLevel));
			if (StringUtils.isNotBlank(selectDestDeptCode(dLevel))) {
				params.add(destDeptCode);
			}
		}
		if (StringUtils.isNotBlank(destZdCode)) { // 目的二级调度
			sql.append(" and DEST_ZD_CODE=?");
			params.add(destZdCode);
		}
		if (StringUtils.isNotBlank(sendZdCode)) { // 寄件二级调度
			sql.append(" and SEND_ZD_CODE=?");
			params.add(sendZdCode);
		}
		if (StringUtils.isNotBlank(destCityCode)) { // 目的城市
			sql.append(" and dest_city_code in(");
			this.appendInCondition(sql, destCityCode, params);
			sql.append(" )");
		}
		if (StringUtils.isNotBlank(cityCode)) { // 寄件城市
			sql.append(" AND  CITY_CODE in(");
			this.appendInCondition(sql, cityCode, params);
			sql.append(" )");
		}
		if (StringUtils.isNotBlank(proType)) { // 产品类型
			sql.append(" and product_type in(");
			this.appendInCondition(sql, proType, params);
			sql.append(" )");
		}
		// 权限校验
		// 如果不是是全网权限且未填写任意网点则需做权限校验
//		if (!userDept.equals("001") && StringUtils.isEmpty(receDeptCode)
//				&& StringUtils.isEmpty(destDeptCode)) {
//			switch (Integer.valueOf(userLevel)) {
//			case 1:
//				sql.append(" and (HQ_CODE = ? OR DEST_HQ_CODE=?)");
//				break;
//			case 2:
//				sql.append(" and (AREA_CODE = ? OR DEST_AREA_CODE=?)");
//				break;
//			}
//			params.add(userDept);
//			params.add(userDept);
//		}
		
		  // 权限验证
        String userId = queryObj.getQueryValue("userId");
		sql.append(" AND (");
		sql.append(" DEST_AREA_CODE in (" ).append(CommonUtil.getUserAllDeptSQL(userId, params)).append(")");
//		 sql.append(" AND DEST_AREA_CODE IN (").append(CommonUtil.getUserAllDeptSQL(userId, params)).append(")");
		
		sql.append(" or");
		sql.append(" AREA_CODE in (").append( CommonUtil.getUserAllDeptSQL(userId, params)).append(")");
		sql.append(")");
		
//        sql.append(")");

		if(StringUtils.isNotEmpty(queryColSQL)){
			// 分组
			if ("1".equals(theTotal)) {
				sql.append(" GROUP BY STAT_DT," + queryColSQL);
			}else if("2".equals(theTotal)){
				sql.append(" GROUP BY " + queryColSQL);
			}
			if(StringUtils.isNotBlank(proType)){
				sql.append(" ,PRODUCT_TYPE");
			}
			
			// 排序
			if ("1".equals(theTotal)) {
				sql.append(" ORDER BY STAT_DT desc," + queryColSQL);
			}else if("2".equals(theTotal)){
				sql.append(" ORDER BY " + queryColSQL);
			}
		}else {
			// 分组
			if ("1".equals(theTotal)) {
				sql.append(" GROUP BY STAT_DT" );
			}
			if(StringUtils.isNotBlank(proType)){
				sql.append(" ,PRODUCT_TYPE");
			}
			
			// 排序
			if ("1".equals(theTotal)) {
				sql.append(" ORDER BY STAT_DT desc" );
			}
		}
		
		
		map.put("sql", sql);
		map.put("params", params.toArray());

		return map;

		

//		if (StringUtils.isNotBlank(proType)) {
//			sql.append(" t.product_type,");
//		}
//
//		if ("5".equals(receiveDeptShowType) && "3".equals(showType)) {
//			// 如果始发地和目的地展示方式都为城市，增加流向类型
//			sql.append(" t.FLOW_TYPE, ");
//		}
//
//		
//
//		// 权限校验
//		// 如果不是是全网权限且未填写任意网点则需做权限校验
//		if (!userDept.equals("001") && StringUtils.isEmpty(receDeptCode)
//				&& StringUtils.isEmpty(destDeptCode)) {
//			switch (Integer.valueOf(userLevel)) {
//			case 1:
//				sql.append(" and (T.HQ_CODE = ? OR T.DEST_HQ_CODE=?)");
//				break;
//			case 2:
//				sql.append(" and (T.AREA_CODE = ? OR T.DEST_AREA_CODE=?)");
//				break;
//			}
//			params.add(userDept);
//			params.add(userDept);
//		}

		
	}

	/**
	 * @param sql
	 *            查询sql
	 * @param showType
	 *            目的地网点展示方式
	 * @param receiveDeptShowType
	 *            寄件网点展示方式
	 * @param theTotal
	 *            汇总方式
	 * @param way
	 *            统计方式
	 */
	protected void addGroupOrderField(StringBuilder sql, String showType,
			String receiveDeptShowType, String theTotal, String way,
			String productType) {
		StringBuilder groupByStr = new StringBuilder();
		StringBuilder orderByStr = null;
		// 汇总方式
		if ("1".equals(theTotal)) {
			groupByStr.append(" T.STAT_DT@,"); // 汇总方式为单日（1），增加寄件日期 ,@符号为替换成DESC
		}

		if (StringUtils.isNotBlank(receiveDeptShowType)) {
			groupByStr.append(appendDeptCode(receiveDeptShowType, false));
		}

		if (StringUtils.isNotBlank(showType)) {
			groupByStr.append(appendCityCode(showType, false));
		}

		if ("2".equals(way)) {
			// 统计方式为“按寄件时段”，增加“时段”
			groupByStr.append(" t.tm_type,");
		}

		if ("4".equals(way) && "5".equals(receiveDeptShowType)
				&& "3".equals(showType)) {
			// 统计方式为“按寄出流向”，增加“流向类型”
			groupByStr.append(" t.flow_type,");
		}
		// 分组
		if (groupByStr.toString().endsWith(",")) {
			groupByStr.deleteCharAt(groupByStr.lastIndexOf(","));
			orderByStr = new StringBuilder(groupByStr);
			if (groupByStr.indexOf("@") > -1) {
				groupByStr.deleteCharAt(groupByStr.indexOf("@"));
			}
			sql.append(" group by ").append(groupByStr);
			if (StringUtils.isNotBlank(productType)) {// 产品类型
				sql.append(" ,t.product_type");
			}
		} else if (StringUtils.isNotBlank(productType)) {
			sql.append(" group by t.product_type");
		}

		// 排序
		if (orderByStr != null && orderByStr.length() > 0) {
			if (orderByStr.indexOf("@") > -1) {
				orderByStr.insert(orderByStr.indexOf("@"), " DESC ")
						.deleteCharAt(orderByStr.indexOf("@"));
			}
			sql.append(" order by ").append(orderByStr);
		}
	}

	private String selectDestDeptCode(String typeLevel) {
		StringBuilder buff = new StringBuilder();
		int type = Integer.parseInt(typeLevel);

		if (type == 1) {
			return buff.append(" and dest_hq_code = ?").toString();
		} else if (type == 2) {
			return buff.append(" and dest_area_code = ?").toString();
		} else {
			return "";
		}
	}

	private String appendCityCode(String showType, boolean bool) {
		StringBuilder buff = new StringBuilder();
		int type = Integer.parseInt(showType);

		if (type >= 1) {
			buff.append(" t.dest_hq_code,");
			if (bool) {
				buff.append(" (select o.dept_name from dim_operation_org o where o.dept_code = t.dest_hq_code) HQ_NAME, ");
			}
			if (type >= 2) {
				buff.append(" t.dest_area_code,");
				if (bool) {
					buff.append(" (select o.dept_name from dim_operation_org o where o.dept_code = t.dest_area_code) AREA_NAME, ");
				}
				if (type >= 3) {
					buff.append(" t.dest_city_code,");
				}
			}
		}
		return buff.toString();
	}

	private String selectDeptCode(String typeLevel) {
		StringBuilder buff = new StringBuilder();

		int type = Integer.parseInt(typeLevel);

		if (type == 1) {
			return buff.append(" and hq_code = ?").toString();
		} else if (type == 2) {
			return buff.append(" and area_code = ?").toString();
		} else if (type == 3) {
			return buff.append(" and division_code = ?").toString();
		} else if (type == 4) {
			return buff.append(" and dept_code = ?").toString();
		} else if (type == 5) {
			return buff.append(" and city_code = ?").toString();
		} else {
			return "";
		}
	}

	private String appendDeptCode(String receiveDeptShowType, boolean bool) {
		StringBuilder buff = new StringBuilder("");
		int type = Integer.parseInt(receiveDeptShowType);

		/*
		 * if(type>=1 || type==0){ buff.append(" t.hq_code,"); if(type>=2){
		 * buff.append(" t.area_code,"); if(type>=3){
		 * buff.append(" t.division_code,"); if(type>=4){
		 * buff.append(" t.dept_code, t.dept_type,"); } } } }
		 */
		if (type == 1) {
			buff.append(" t.hq_code,");
			if (bool) {
				buff.append(" (select o.dept_name from dim_operation_org o where o.dept_code = t.hq_code) HQ_NAME, ");
			}
		}
		if (type == 2) {
			buff.append(" t.hq_code,");
			buff.append(" t.area_code,");
			if (bool) {
				buff.append(" (select o.dept_name from dim_operation_org o where o.dept_code = t.hq_code) HQ_NAME, ");
				buff.append(" (select o.dept_name from dim_operation_org o where o.dept_code = t.area_code) AREA_NAME, ");
			}
		}

		if (type == 3) {
			buff.append(" t.hq_code,");
			buff.append(" t.area_code,");
			buff.append(" t.city_code,");
			buff.append(" t.division_code,");
		}

		if (type == 4) {
			buff.append(" t.hq_code,");
			buff.append(" t.area_code,");
			buff.append(" t.division_code,");
			buff.append(" t.city_code,");
			if (bool) {
				buff.append(" (SELECT NVL(D.CLASS_NAME, D.CLASS_CODE) CLASS_NAME  FROM TM_CDP_DEPT_TYPE D WHERE T.DEPT_TYPE = D.TYPE_CODE) DEPT_NAME, ");
				buff.append(" (SELECT  NVL(D.TYPE_NAME, D.TYPE_CODE) CLASS_NAME FROM TM_CDP_DEPT_TYPE D WHERE T.DEPT_TYPE = D.TYPE_CODE) CHILD_DEPT_NAME, ");
				buff.append(" (select dd.dept_name from tm_department dd where dd.dept_code = t.dept_code) dept_code_name,");
			}
			buff.append(" t.dept_code, t.dept_type,");
		}

		if (type == 5) {
			buff.append(" t.hq_code,");
			buff.append(" t.area_code,");
			buff.append(" t.city_code,");
		}

		return buff.toString();
	}

	private void appendInCondition(StringBuilder sql, String queryValue,
			List<Object> params) {
		String formNameArray[] = queryValue.split(",");
		for (int i = 0, k = formNameArray.length; i < k; i++) {
			sql.append("?");
			params.add(formNameArray[i]);
			if (i != k - 1) {
				sql.append(",");
			}
		}
	}

	private String createSQLByLevel(String receiveDeptShowType,
			String destDeptShowType) {

		// 没有值则不做统计
		Integer receiveDeptType = StringUtils.isEmpty(receiveDeptShowType) ? 0
				: Integer.valueOf(receiveDeptShowType);
		// 没有值则不做统计
		Integer destDeptType = StringUtils.isEmpty(destDeptShowType) ? 0
				: Integer.valueOf(destDeptShowType);

		String sql = "";
		switch (receiveDeptType) {
		case 1:
			sql = "SEND_BG_CODE";
			break;
		case 2:
			sql = "SEND_BG_CODE,HQ_CODE";
			break;
		case 3:
			sql = "SEND_ZD_CODE";
			break;
		case 4:
			sql = "SEND_BG_CODE,HQ_CODE,AREA_CODE";
			break;
		case 5:
			sql = "SEND_BG_CODE,HQ_CODE,SEND_ZD_CODE,AREA_CODE,CITY_CODE";
			break;
		case 6:
			sql = "SEND_ZD_CODE,CITY_CODE";
			break;
		case 7:
			sql = "SEND_BG_CODE,HQ_CODE,SEND_ZD_CODE,AREA_CODE,CITY_CODE,DIVISION_CODE";
			break;
		case 8:
			sql = "SEND_BG_CODE,HQ_CODE,SEND_ZD_CODE,AREA_CODE,CITY_CODE,DIVISION_CODE,DEPT_CODE,DEPT_CODE_NAME,DEPT_NAME,CHILD_DEPT_NAME";
			break;
		default:
			break;
		}
		if(!sql.equals("")&& destDeptType!=0){
			sql += ",";
		}
		switch (destDeptType) {
		case 1:
			sql += "DEST_BG_CODE";
			break;
		case 2:
			sql += "DEST_BG_CODE,DEST_HQ_CODE";
			break;
		case 3:
			sql += "DEST_ZD_CODE";
			break;
		case 4:
			sql += "DEST_BG_CODE,DEST_HQ_CODE,DEST_AREA_CODE";
			break;
		case 5:
			sql += "DEST_BG_CODE,DEST_HQ_CODE,DEST_ZD_CODE,DEST_AREA_CODE,DEST_CITY_CODE";
			break;
		case 6:
			sql += "DEST_ZD_CODE,DEST_CITY_CODE";
			break;
		default:
			break;
		}
		if((receiveDeptType==5||receiveDeptType==6)&&(destDeptType==5||destDeptType==6)){
			sql+=",FLOW_TYPE";
		}
		return sql;
	}

}