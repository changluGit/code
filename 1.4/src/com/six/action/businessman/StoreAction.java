package com.six.action.businessman;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import com.six.action.BaseAction;
import com.six.entity.Businessman;
import com.six.entity.Store;
import com.six.service.BusinessInfoServie;
import com.six.service.ImgService;
import com.six.service.StoreService;

public class StoreAction extends BaseAction{
	
	private List<File> file;
	private List<String> fileFileName;
	private Businessman businessman;
	private Store store;
	private StoreService storeservice;
	private BusinessInfoServie businessInfoService;
	@Override
	public Object getModel() {
		// TODO Auto-generated method stub
		return null;
	}
	
	// 得到系统时间字符串
	public String getSysTime() {
		Date date = new Date();
		String temp = date.toString().replaceAll(" ", "");
		temp = temp.replaceAll(":", "");
		System.out.println(temp);
		return temp;
	}
	public String gotoStore(){
		HttpServletRequest request = ServletActionContext.getRequest();
		businessman= (Businessman)getRequest().getSession().getAttribute("loginedBussinessman");
		store=businessman.getStore();
		store = storeservice.get(store.getId());
		request.setAttribute("store", store);
		return "showstore";
	}
	public String storeModify() throws Exception
	{
		Timestamp open=null;
		Timestamp stop=null;
		String ss="";   //修改成功提示
		String s1="";    //修改失败提示
		String pictureAddress = "";
		String name=getRequest().getParameter("name");
		String tel=getRequest().getParameter("tel");
		String logo=getRequest().getParameter("file");
		String addr=getRequest().getParameter("addr");
		String type=getRequest().getParameter("type");
		String gong=getRequest().getParameter("gong");
		int speed=Integer.parseInt(getRequest().getParameter("speed"));
		String taste=getRequest().getParameter("taste");
		String btime=getRequest().getParameter("open");
		String etime=getRequest().getParameter("stop");
		HttpServletRequest request = ServletActionContext.getRequest();
		businessman= (Businessman)getRequest().getSession().getAttribute("loginedBussinessman");
		store=businessman.getStore();	
		//开店时间、结束时间修改
		if(!("".equals(btime)) && !("".equals(etime)))
		{
			open=Timestamp.valueOf(btime);
			stop=Timestamp.valueOf(etime);
			if(open.compareTo(stop)<0)
			{
				if(null!=open && !(open.equals(store.getBeginSaleHour())) ){
					store.setBeginSaleHour(open);
					storeservice.updateStore(store);
					ss+="营业时间、";
				}
				
				if(null!=stop && !(stop.equals(store.getEndSaleTime()))){
					store.setEndSaleTime(stop);
					storeservice.updateStore(store);
					ss+="打烊时间、";
				}
			}
			else
			{
				s1+="营业时间不能大于打烊时间";
			}
		}
		/*else
		{
			s1+="营业时间、";
		}*/
		//店铺名修改
		if(!"".equals(name) && (name.length()<16 && name.length()>3) && !(name.equals(store.getStoreName()))){
			store.setStoreName(name);
			storeservice.updateStore(store);
			ss+="店铺名称、";
		}
		/*else{
			s1+="店铺名称、";
		}*/
		//电话号码
		if(!"".equals(tel) && (tel.length()==11) && !(tel.equals(store.getTelephone()))){
			store.setTelephone(tel);
			storeservice.updateStore(store);
			ss+="电话号、";
		}
		/*else
		{
			s1+="电话号、";
		}*/
		//logo
		String path = ServletActionContext.getServletContext().getRealPath("/")+"\\StoreLogo";
		storeservice.makeDir(path);

		String imgPagePath = "StoreLogo/";
		if (null != file) {
			
			InputStream is = new FileInputStream(file.get(0));

			System.out.println(path);
			String tag=getSysTime() + store.getId() + ".jpg";
			File destFile = new File(path,tag);

			OutputStream os = new FileOutputStream(destFile);

			byte[] buffer = new byte[400];

			int length = 0;

			while ((length = is.read(buffer)) > 0) {
				os.write(buffer, 0, length);
			}
			is.close();
			os.close();
			pictureAddress = imgPagePath + tag;
			String srcImgpath = path + pictureAddress;
			ImgService.scale(srcImgpath, srcImgpath, 100, 100);
			if(!(pictureAddress.equals(store.getLogoAddress()))){
				store.setLogoAddress(pictureAddress);
				storeservice.updateStore(store);
				ss+="店铺logo、";
			}
			/*else{
				s1+="店铺logo、";
			}*/
		}
		//店铺地址
		if(!"".equals(addr) && !(addr.equals(store.getBusinessAddress()))){
			store.setBusinessAddress(addr);
			storeservice.updateStore(store);
			ss+="店铺地址、";
		}
		/*else{
			s1+="店铺地址、";
		}*/
		//送餐速度
		if(speed>0 && !(speed==store.getFoodDeliveryTime())){
			store.setFoodDeliveryTime(speed);
			storeservice.updateStore(store);
			ss+="送餐速度、";
		}
		/*else{
			s1+="送餐速度、";
		}
		//店铺类型
*/		if(!"".equals(type) && !(type.equals(store.getStoreCategory()))){
			store.setStoreCategory(type);
			storeservice.updateStore(store);
			ss+="店铺类型、";
		}
	/*	else{
			s1+="店铺类型、";
		}*/
		//公告
		if(!"".equals(gong) && !(gong.equals(store.getMerchantsAnnouncement()))){
			store.setMerchantsAnnouncement(gong);
			storeservice.updateStore(store);
			ss+="公告、";
		}
		/*else{
			s1+="公告、";
		}*/
		//口味
		if(!"".equals(taste) && !(taste.equals(store.getTaste()))){
			store.setTaste(taste);
			storeservice.updateStore(store);
			ss+="口味、";
		}
		/*else{
			s1+="口味、";
		}*/
		//提示信息
		int lenght=ss.length()-1;
		if("".equals(ss)){
			request.setAttribute("tishi", "恭喜修改失败!您并没有做任何修改");
		}
		else{
			String s2="";
			if(ss.endsWith("、"))
			{
				s2=ss.substring(0, lenght);
				request.setAttribute("tishi", "恭喜修改成功!您本次更新了:"+s2);
			}
			else{
				request.setAttribute("tishi", "恭喜修改成功!您本次更新了:"+ss);
			}
		}
		request.setAttribute("store", store);
		return "success";
	}
	public StoreService getStoreService() {
		return storeservice;
	}
	@Resource
	public void setStoreService(StoreService storeService) {
		this.storeservice = storeService;
	}

	public List<File> getFile() {
		return file;
	}

	public void setFile(List<File> file) {
		this.file = file;
	}

	public List<String> getFileFileName() {
		return fileFileName;
	}

	public void setFileFileName(List<String> fileFileName) {
		this.fileFileName = fileFileName;
	}
	

}

