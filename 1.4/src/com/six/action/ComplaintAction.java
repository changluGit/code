package com.six.action;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.springframework.stereotype.Component;

import com.six.entity.Complaint;
import com.six.entity.User;
import com.six.service.ComplaintServer;

@Component
public class ComplaintAction extends BaseAction{
	
	private Logger logger = Logger.getLogger(ComplaintAction.class);
	
	private ComplaintServer complaintServer;
	private Complaint complaint;

	@Override
	public Object getModel()
	{
		if(null == complaint)
			complaint = new Complaint();
		return complaint;
	}
	//添加评论
	public String addComplaint() throws IOException
	{
		 HttpServletRequest request = ServletActionContext.getRequest();
		 String complaint_cont=getRequest().getParameter("complaint_cont");
		// String businessman=getRequest().getParameter("business_contman_id");
		 User user= (User)getRequest().getSession().getAttribute("loginedUser");
		 Date createTime = new Date(System.currentTimeMillis());
		 Timestamp ts = new Timestamp(createTime.getTime());
		 complaint.setCreatTime(ts);
		 complaint.setUser(user);
		 complaint.setContent(complaint_cont);
		 complaint.setStatus("0");
		 complaintServer.save(complaint);
		 List<Complaint> list = complaintServer.findAll(user);
		 Collections.reverse(list);
		 request.setAttribute("result", list);
		 return "input";
		
	}
	
	//显示所有complaint
	public String listComp() throws Exception
	{
		logger.info("in");
		HttpServletRequest request = ServletActionContext.getRequest();
		User user= (User)getRequest().getSession().getAttribute("loginedUser");
		List<Complaint> list = complaintServer.findAll(user);
		if(list!=null){
			Collections.reverse(list);
			request.setAttribute("result", list);
		}
		else
			request.setAttribute("result",null);
		return "input";

	}

	public ComplaintServer getComplaintServer() {
		return complaintServer;
	}
	@Resource
	public void setComplaintServer(ComplaintServer complaintServer) {
		this.complaintServer = complaintServer;
	}

}
